package com.muzi.adtool.services;

import com.muzi.adtool.dao.*;
import com.muzi.adtool.domain.*;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Repository
public class AdminServiceImpl implements AdminService{

    @Resource(name="uploadPath")
    String uploadPath;

    @Autowired
    AdminDAO adminDAO;

    @Autowired
    JoinedDAO joinedDAO;

    @Autowired
    CampaignDAO campaignDAO;

    @Autowired
    BrandDAO brandDAO;

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    MemberDAO memberDAO;


    @Override
    @Transactional
    public int joined_member_list_update(JoinedVO vo) {
        adminDAO.joined_member_chat_update(vo);
        return adminDAO.joined_member_info_update(vo);
    }

    @Override
    @Transactional
    public int status_update_and_payment_insert(JoinedVO vo) {
        PaymentVO oldPvo=adminDAO.payment_read_by_joined_seq(vo.getJoined_campaign_seq());
        //System.out.println(oldPvo.getAccept());
        //System.out.println(oldPvo.getPayment_status());
        PaymentVO newPvo=new PaymentVO();

        if(vo.getAccept().equals("accept")){
            if(oldPvo.getPayment_seq()==null){
                newPvo.setPayment_seq(getSequenceService.getSeq("paym"));
                newPvo.setJoined_campaign_seq(vo.getJoined_campaign_seq());
                adminDAO.payment_insert(newPvo);
            }
        }else if(vo.getAccept().equals("decline")){
            if(oldPvo.getPayment_seq()!=null){
                if(oldPvo.getPayment_status().equals("Send")){
                    adminDAO.payment_delete(vo.getJoined_campaign_seq());
                }
            }
        }
        return adminDAO.joined_member_accept_update(vo);
        //return 1;
    }

    @Override
    @Transactional
    public int reservation_campaign_insert(Reservation_campaignVO vo) {
        vo.setRe_campaign_seq(getSequenceService.getSeq("rcam"));
        vo.setStatus("recruit");
        return adminDAO.reservation_campaign_insert(vo);
    }

    // 관리자페이지 - 회원관리에서 친구추천으로 포인트를 적립 시켜줄 때
    @Override
    @Transactional
    public int insert_point_by_recommender(PointVO vo) {
        BasicVO basicVO = adminDAO.find_basic_by_seq();
        Member_informationVO member_informationVO = memberDAO.infoReadByMemberSeq(vo.getMember_seq());
        adminDAO.update_point_status(vo.getMember_seq());
        vo.setPoint_seq(getSequenceService.getSeq("poin"));
        vo.setStatus(1);
        vo.setPoint(basicVO.getPoint());
        vo.setType("적립");
        vo.setRequest_date(member_informationVO.getJoin_date());
        vo.setDetail("추천인 (" + member_informationVO.getRecommender() + ")");

        PointVO recommenderVO = new PointVO();
        recommenderVO.setMember_seq(memberDAO.findByMemberId(member_informationVO.getRecommender()).getMember_seq());
        recommenderVO.setPoint_seq(getSequenceService.getSeq("poin"));
        recommenderVO.setStatus(1);
        recommenderVO.setPoint(basicVO.getPoint());
        recommenderVO.setType("적립");
        recommenderVO.setDetail("reward (" + member_informationVO.getMember_id() + ")");
        adminDAO.insert_point_by_admin(recommenderVO);
        return adminDAO.insert_point_by_admin(vo);
    }

    @Override
    @Transactional
    public int insert_point_by_campaign(PointVO vo) {
        int result = 0;

        // 추천인 포인트 지급 로직
        Member_informationVO memberInformationVO = memberDAO.infoReadByMemberSeq(vo.getMember_seq());
        if(!memberInformationVO.getRecommender().equals("") && memberInformationVO.getRecommender()!=null && memberInformationVO.getPoint_status()==0) { // 추천인 포인트가 지급 되지 않았으면,
            // 추천인 ID가 실제 존재하는지 여부 확인
            MemberVO recommender = memberDAO.findByMemberId(memberInformationVO.getRecommender());
            if(recommender==null) { // 추천인 Id가 존재하지 않거나 잘못 되었을 경우
                result=2;
            }else {
                BasicVO basicVO = adminDAO.find_basic_by_seq();
                adminDAO.update_point_status(vo.getMember_seq());
                vo.setPoint_seq(getSequenceService.getSeq("poin"));
                vo.setStatus(1);
                vo.setPoint(basicVO.getPoint());
                vo.setType("적립");
                vo.setRequest_date(memberInformationVO.getJoin_date());
                vo.setDetail("추천인 (" + memberInformationVO.getRecommender() + ")");
                adminDAO.insert_point_by_admin(vo);

                PointVO recommenderVO = new PointVO();
                recommenderVO.setMember_seq(recommender.getMember_seq());
                recommenderVO.setPoint_seq(getSequenceService.getSeq("poin"));
                recommenderVO.setStatus(1);
                recommenderVO.setPoint(basicVO.getPoint());
                recommenderVO.setType("적립");
                recommenderVO.setDetail("reward (" + memberInformationVO.getMember_id() + ")");
                adminDAO.insert_point_by_admin(recommenderVO);
                result=1;
            }
        }else {
            result=1;
        }

        CampaignVO campaignVO = campaignDAO.campaign_read_by_seq(vo.getCampaign_seq());
        vo.setPoint_seq(getSequenceService.getSeq("poin"));
        vo.setType("적립");
        vo.setDetail("캠페인 (" + campaignVO.getCampaign_title() + ")");
        vo.setStatus(1);
        vo.setPoint(vo.getPoint());
        adminDAO.insert_point_by_admin(vo);
        return result;
    }

    @Override
    @Transactional
    public int withdraw_point_by_done(PointVO vo) {
        int result = 0;
        CampaignVO campaignVO = campaignDAO.campaign_read_by_seq(vo.getCampaign_seq());
        double totalPoint = memberDAO.totalPoint_of_member(vo.getMember_seq());
        double point = vo.getPoint();
        if(point > totalPoint){
            result = 0;
        }else {
            vo.setPoint_seq(getSequenceService.getSeq("poin"));
            vo.setType("회수");
            vo.setDetail("캠페인 (" + campaignVO.getCampaign_title() + ")");
            vo.setStatus(1);
            vo.setPoint(point * -1);
            adminDAO.insert_point_by_admin(vo);
            result = 1;
        }

        return result;
    }

    @Override
    public int brand_delete(String brand_seq) {
        return 0;
    }

    // 가입회원관리 엑셀
    @Override
    @Transactional
    public void memberExcelDownload(Criteria cri, HttpServletResponse response) {
        int totalCount = adminDAO.member_all_count_except_for_admin(cri);
        cri.setDesc("asc");
        cri.setPerPageNum(totalCount);
        List<Member_informationVO> list = adminDAO.member_all_list_except_for_admin(cri);

        FileOutputStream fileOutputStream = null;

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =  workbook.createSheet("가입회원");
        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum ++);
        headerRow.createCell(0).setCellValue("순번");
        headerRow.createCell(1).setCellValue("이메일 주소(아이디)");
        headerRow.createCell(2).setCellValue("이름");
        headerRow.createCell(3).setCellValue("국가 1");
        headerRow.createCell(4).setCellValue("국가 2");
        headerRow.createCell(5).setCellValue("국가 3");
        headerRow.createCell(6).setCellValue("연락처");
        headerRow.createCell(7).setCellValue("Address 1");
        headerRow.createCell(8).setCellValue("Address 2");
        headerRow.createCell(9).setCellValue("State");
        headerRow.createCell(10).setCellValue("City");
        headerRow.createCell(11).setCellValue("Zipcode");
        headerRow.createCell(12).setCellValue("Shopee ID");
        headerRow.createCell(13).setCellValue("Lazada ID");
        headerRow.createCell(14).setCellValue("Instagram");
        headerRow.createCell(15).setCellValue("I_followers");
        headerRow.createCell(16).setCellValue("Youtube");
        headerRow.createCell(17).setCellValue("Y_subscribes");
        headerRow.createCell(18).setCellValue("Facebook");
        headerRow.createCell(19).setCellValue("F_followers");
        headerRow.createCell(20).setCellValue("Tiktok");
        headerRow.createCell(21).setCellValue("T_followers");
        headerRow.createCell(22).setCellValue("Paypal email");
        headerRow.createCell(23).setCellValue("Payoneer email");
        headerRow.createCell(24).setCellValue("Preferences");
        headerRow.createCell(25).setCellValue("가입일");
        headerRow.createCell(26).setCellValue("추천인");
        headerRow.createCell(27).setCellValue("추천 포인트 지급");
        headerRow.createCell(28).setCellValue("블랙리스트 설정일");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int no = 1;
        for(Member_informationVO vo : list) {
            Row contentRow = sheet.createRow(rowNum ++);
            contentRow.createCell(0).setCellValue(no);
            if(vo.getBanned()==0) {
                contentRow.createCell(1).setCellValue(vo.getMember_id());
            }else {
                contentRow.createCell(1).setCellValue(vo.getMember_id() + " (B)");
            }
            contentRow.createCell(2).setCellValue(vo.getMember_name());
            contentRow.createCell(3).setCellValue(vo.getCountry());
            if(vo.getCountry2()!=null && !vo.getCountry().equals("")){
                contentRow.createCell(4).setCellValue(vo.getCountry2());
            }else{
                contentRow.createCell(4).setCellValue("-");
            }
            if(vo.getCountry3()!=null && !vo.getCountry3().equals("")){
                contentRow.createCell(5).setCellValue(vo.getCountry3());
            }else {
                contentRow.createCell(5).setCellValue("-");
            }
            contentRow.createCell(6).setCellValue(vo.getNational_number()+vo.getPhone());
            contentRow.createCell(7).setCellValue(vo.getAddress1());
            contentRow.createCell(8).setCellValue(vo.getAddress2());
            contentRow.createCell(9).setCellValue(vo.getState());
            contentRow.createCell(10).setCellValue(vo.getCity());
            contentRow.createCell(11).setCellValue(vo.getZipcode());
            contentRow.createCell(12).setCellValue(vo.getShopee_id());
            contentRow.createCell(13).setCellValue(vo.getLazada_id());
            contentRow.createCell(14).setCellValue(vo.getInstagram_url());
            contentRow.createCell(15).setCellValue(followers(vo.getInstagram_followers()));
            contentRow.createCell(16).setCellValue(vo.getYoutube_url());
            contentRow.createCell(17).setCellValue(followers(vo.getYoutube_subscribes()));
            contentRow.createCell(18).setCellValue(vo.getFacebook_url());
            contentRow.createCell(19).setCellValue(followers(vo.getFacebook_followers()));
            contentRow.createCell(20).setCellValue(vo.getTiktok_url());
            contentRow.createCell(21).setCellValue(followers(vo.getTiktok_followers()));
            contentRow.createCell(22).setCellValue(vo.getPaypal_id());
            contentRow.createCell(23).setCellValue(vo.getPayoneer_id());
            contentRow.createCell(24).setCellValue(vo.getPreferences());
            if(vo.getJoin_date()!=null) {
                contentRow.createCell(25).setCellValue(sdf.format(vo.getJoin_date()));
            }else {
                contentRow.createCell(25).setCellValue("-");
            }
            contentRow.createCell(26).setCellValue(vo.getRecommender());
            if(vo.getPoint_status()==0) {
                contentRow.createCell(27).setCellValue("지급");
            }else {
                contentRow.createCell(27).setCellValue("지급 완료");
            }
            if(vo.getBanned_date()!=null) {
                contentRow.createCell(28).setCellValue(sdf.format(vo.getBanned_date()));
            }else {
                contentRow.createCell(28).setCellValue("-");
            }
            no++;
        }

        for (int i=0; i<29; i++){
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 512);
        }

        try {
            //String now = String.valueOf(System.currentTimeMillis());
            File dir = new File(uploadPath + "temp/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileOutputStream = new FileOutputStream(uploadPath + "temp/" + "member_list" + ".xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

            File file = new File(uploadPath + "temp/" + "member_list" + ".xlsx");

            FileInputStream in = null;

            in = new FileInputStream(file);
            String name = "member_list" + ".xlsx";

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" +
                    new String(
                            name.getBytes("UTF-8"),
                            "ISO-8859-1") +
                    "\";");

            BufferedInputStream bis = new BufferedInputStream(in);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            byte[] data = new byte[2048];
            int input = 0;
            while ((input = bis.read(data)) != -1) {
                bos.write(data, 0, input);
                bos.flush();
            }

            if (bos != null) bos.close();
            if (bis != null) bis.close();
            if (so != null) so.close();
            if (in != null) in.close();

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }

            if (!file.delete()) {
//                log.error(file.getName() + " 삭제에 실패했습니다.");
            } else {
//                log.info(file.getName() + " 삭제되었습니다.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    // 참가자 명단 엑셀
    @Override
    @Transactional
    public void joinedMemberExcelDownload(String campaign_seq, HttpServletResponse response) {
        List<PaymentVO> list = adminDAO.joined_member_list_by_campaign_seq(campaign_seq);



        FileOutputStream fileOutputStream = null;

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =  workbook.createSheet("joined_member_list");
        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum ++);
        headerRow.createCell(0).setCellValue("순번");
        headerRow.createCell(1).setCellValue("타임스탬프");
        headerRow.createCell(2).setCellValue("이메일 주소");
        headerRow.createCell(3).setCellValue("*승인여부*");
        headerRow.createCell(4).setCellValue("채팅");
        headerRow.createCell(5).setCellValue("선호 카테고리");
        headerRow.createCell(6).setCellValue("이름");
        headerRow.createCell(7).setCellValue("연락처");
        headerRow.createCell(8).setCellValue("국가 1");
        headerRow.createCell(9).setCellValue("국가 2");
        headerRow.createCell(10).setCellValue("국가 3");
        headerRow.createCell(11).setCellValue("Address 1");
        headerRow.createCell(12).setCellValue("Address 2");
        headerRow.createCell(13).setCellValue("State");
        headerRow.createCell(14).setCellValue("City");
        headerRow.createCell(15).setCellValue("Zipcode");
        headerRow.createCell(16).setCellValue("Shopee ID");
        headerRow.createCell(17).setCellValue("Lazada ID");
        headerRow.createCell(18).setCellValue("Instagram");
        headerRow.createCell(19).setCellValue("Youtube");
        headerRow.createCell(20).setCellValue("Facebook");
        headerRow.createCell(21).setCellValue("Tiktok");
        headerRow.createCell(22).setCellValue("Paypal email");
        headerRow.createCell(23).setCellValue("Payoneer email");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int no = 1;
        String chat = "";
        for(PaymentVO vo : list) {
            Row contentRow = sheet.createRow(rowNum ++);
            contentRow.createCell(0).setCellValue(no);
            contentRow.createCell(1).setCellValue(sdf.format(vo.getJoined_date()));
            if(vo.getBanned()==0) {
                contentRow.createCell(2).setCellValue(vo.getMember_id());
            }else if(vo.getBanned()==1) {
                contentRow.createCell(2).setCellValue(vo.getMember_id() + "  (B)");
            }
            contentRow.createCell(3).setCellValue(vo.getAccept());
            if(vo.getChat_check()==0){
                chat = "";
            }else {
                chat = "V";
            }
            contentRow.createCell(4).setCellValue(chat);
            contentRow.createCell(5).setCellValue(vo.getPreferences());
            contentRow.createCell(6).setCellValue(vo.getMember_name());
            contentRow.createCell(7).setCellValue(vo.getNational_number()+vo.getPhone());
            contentRow.createCell(8).setCellValue(vo.getCountry());
            if(vo.getCountry2()!=null && !vo.getCountry2().equals("")) {
                contentRow.createCell(9).setCellValue(vo.getCountry2());
            }else {
                contentRow.createCell(9).setCellValue("-");
            }
            if(vo.getCountry3()!=null && !vo.getCountry3().equals("")) {
                contentRow.createCell(10).setCellValue(vo.getCountry3());
            }else {
                contentRow.createCell(10).setCellValue("-");
            }
            contentRow.createCell(11).setCellValue(vo.getAddress1());
            contentRow.createCell(12).setCellValue(vo.getAddress2());
            contentRow.createCell(13).setCellValue(vo.getState());
            contentRow.createCell(14).setCellValue(vo.getCity());
            contentRow.createCell(15).setCellValue(vo.getZipcode());
            contentRow.createCell(16).setCellValue(vo.getShopee_id());
            contentRow.createCell(17).setCellValue(vo.getLazada_id());
            contentRow.createCell(18).setCellValue(vo.getInstagram_url());
            contentRow.createCell(19).setCellValue(vo.getYoutube_url());
            contentRow.createCell(20).setCellValue(vo.getFacebook_url());
            contentRow.createCell(21).setCellValue(vo.getTiktok_url());
            contentRow.createCell(22).setCellValue(vo.getPaypal_id());
            contentRow.createCell(23).setCellValue(vo.getPayoneer_id());
            no++;
        }

        for (int i=0; i<24; i++){
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 512);
        }

        try {
            //String now = String.valueOf(System.currentTimeMillis());
            File dir = new File(uploadPath + "temp/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileOutputStream = new FileOutputStream(uploadPath + "temp/" + "joined_member_list" + ".xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

            File file = new File(uploadPath + "temp/" + "joined_member_list" + ".xlsx");

            FileInputStream in = null;

            in = new FileInputStream(file);
            String name = "joined_member_list" + ".xlsx";

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" +
                    new String(
                            name.getBytes("UTF-8"),
                            "ISO-8859-1") +
                    "\";");

            BufferedInputStream bis = new BufferedInputStream(in);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            byte[] data = new byte[2048];
            int input = 0;
            while ((input = bis.read(data)) != -1) {
                bos.write(data, 0, input);
                bos.flush();
            }

            if (bos != null) bos.close();
            if (bis != null) bis.close();
            if (so != null) so.close();
            if (in != null) in.close();

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }

            if (!file.delete()) {
//                log.error(file.getName() + " 삭제에 실패했습니다.");
            } else {
//                log.info(file.getName() + " 삭제되었습니다.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    // 캠페인 리뷰 엑셀
    @Override
    @Transactional
    public void campaignReviewExcel(String campaign_seq, HttpServletResponse response) throws IOException {
        List<PaymentVO> list = adminDAO.joined_member_list_by_campaign_seq(campaign_seq);

        FileOutputStream fileOutputStream = null;

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =  workbook.createSheet("campaign_review");
        int rowNum = 0;
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 10));
        Row headerRow = sheet.createRow(rowNum ++);
        headerRow.createCell(0).setCellValue("순번");
        headerRow.createCell(1).setCellValue("아이디");
        headerRow.createCell(2).setCellValue("연락처");
        headerRow.createCell(3).setCellValue("리뷰 스크린샷");

        int no = 1;

        for(PaymentVO vo : list) {
            Row contentRow = sheet.createRow(rowNum ++);
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 9, 10));
            contentRow.createCell(0).setCellValue(no);
            contentRow.createCell(1).setCellValue(vo.getMember_id());
            contentRow.createCell(2).setCellValue(vo.getNational_number()+" "+vo.getPhone());
            contentRow.createCell(3).setCellValue("");
            contentRow.createCell(5).setCellValue("");
            contentRow.createCell(7).setCellValue("");
            contentRow.createCell(9).setCellValue("");
            no++;

            if(vo.getReview_img1()!=null && !vo.getReview_img1().equals("")) {
//                System.out.println("review_1");
                cellPic(vo.getJoined_campaign_seq(), vo.getReview_img1(), workbook, sheet, rowNum-1, 3);
            }
            if(vo.getReview_img2()!=null && !vo.getReview_img2().equals("")) {
//                System.out.println("review_2");
                cellPic(vo.getJoined_campaign_seq(), vo.getReview_img2(), workbook, sheet, rowNum-1, 5);
            }
            if(vo.getReview_img3()!=null && !vo.getReview_img3().equals("")) {
//                System.out.println("review_3");
                cellPic(vo.getJoined_campaign_seq(), vo.getReview_img3(), workbook, sheet, rowNum-1, 7);
            }
            if(vo.getReview_img4()!=null && !vo.getReview_img4().equals("")) {
//                System.out.println("review_4");
                cellPic(vo.getJoined_campaign_seq(), vo.getReview_img4(), workbook, sheet, rowNum-1, 9);
            }
        }

        for (int i=0; i<10; i++){
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 512);
            sheet.setDefaultRowHeightInPoints(80);
        }

        try {
            //String now = String.valueOf(System.currentTimeMillis());
            File dir = new File(uploadPath + "temp/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileOutputStream = new FileOutputStream(uploadPath + "temp/" + "review" + ".xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

            File file = new File(uploadPath + "temp/" + "review" + ".xlsx");

            FileInputStream in = null;

            in = new FileInputStream(file);
            String name = "review" + ".xlsx";

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" +
                    new String(
                            name.getBytes("UTF-8"),
                            "ISO-8859-1") +
                    "\";");

            BufferedInputStream bis = new BufferedInputStream(in);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            byte[] data = new byte[2048];
            int input = 0;
            while ((input = bis.read(data)) != -1) {
                bos.write(data, 0, input);
                bos.flush();
            }

            if (bos != null) bos.close();
            if (bis != null) bis.close();
            if (so != null) so.close();
            if (in != null) in.close();

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }

            if (!file.delete()) {
//                log.error(file.getName() + " 삭제에 실패했습니다.");
            } else {
//                log.info(file.getName() + " 삭제되었습니다.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    // 캠페인 정산 엑셀
    @Override
    @Transactional
    public void campaignCalExcel(String campaign_seq, HttpServletResponse response) throws IOException {
        List<PaymentVO> list = adminDAO.joined_member_list_by_campaign_seq(campaign_seq);

        FileOutputStream fileOutputStream = null;

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =  workbook.createSheet("campaign_payment");
        int rowNum = 0;
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 10));
        Row headerRow = sheet.createRow(rowNum ++);
        headerRow.createCell(0).setCellValue("순번");
        headerRow.createCell(1).setCellValue("아이디");
        headerRow.createCell(2).setCellValue("연락처");
        headerRow.createCell(3).setCellValue("리뷰 스크린샷");
        headerRow.createCell(11).setCellValue("Expense");
        headerRow.createCell(12).setCellValue("Exchange");
        headerRow.createCell(13).setCellValue("Compensation");
        headerRow.createCell(14).setCellValue("Fee");
        headerRow.createCell(15).setCellValue("Total");
        headerRow.createCell(16).setCellValue("SEND");

        int no = 1;

        for(PaymentVO vo : list) {
            Row contentRow = sheet.createRow(rowNum ++);
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 9, 10));
            contentRow.createCell(0).setCellValue(no);
            contentRow.createCell(1).setCellValue(vo.getMember_id());
            contentRow.createCell(2).setCellValue(vo.getNational_number()+" "+vo.getPhone());
            contentRow.createCell(3).setCellValue("");
            contentRow.createCell(5).setCellValue("");
            contentRow.createCell(7).setCellValue("");
            contentRow.createCell(9).setCellValue("");
            contentRow.createCell(11).setCellValue(vo.getExpense());
            contentRow.createCell(12).setCellValue(vo.getExchange_rate());
            contentRow.createCell(13).setCellValue(vo.getCompensation());
            contentRow.createCell(14).setCellValue(vo.getFee());
            contentRow.createCell(15).setCellValue(vo.getTotal());
            String pStatus="";
            if(vo.getPayment_status().equals("Send") || vo.getPayment_status().equals("notPay")){
                pStatus="SEND";
            }else{
                pStatus="DONE";
            }
            contentRow.createCell(16).setCellValue(vo.getPayment_status());
            no++;

            if(vo.getPayment_img1()!=null && !vo.getPayment_img1().equals("")) {
//                System.out.println("review_1");
                cellPic(vo.getJoined_campaign_seq(), vo.getPayment_img1(), workbook, sheet, rowNum-1, 3);
            }
            if(vo.getPayment_img2()!=null && !vo.getPayment_img2().equals("")) {
//                System.out.println("review_2");
                cellPic(vo.getJoined_campaign_seq(), vo.getPayment_img2(), workbook, sheet, rowNum-1, 5);
            }
            if(vo.getPayment_img3()!=null && !vo.getPayment_img3().equals("")) {
//                System.out.println("review_3");
                cellPic(vo.getJoined_campaign_seq(), vo.getPayment_img3(), workbook, sheet, rowNum-1, 7);
            }
            if(vo.getPayment_img4()!=null && !vo.getPayment_img4().equals("")) {
//                System.out.println("review_4");
                cellPic(vo.getJoined_campaign_seq(), vo.getPayment_img4(), workbook, sheet, rowNum-1, 9);
            }
        }

        for (int i=0; i<10; i++){
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 512);
            sheet.setDefaultRowHeightInPoints(80);
        }

        try {
            //String now = String.valueOf(System.currentTimeMillis());
            File dir = new File(uploadPath + "temp/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileOutputStream = new FileOutputStream(uploadPath + "temp/" + "payment" + ".xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.flush();

            File file = new File(uploadPath + "temp/" + "payment" + ".xlsx");

            FileInputStream in = null;

            in = new FileInputStream(file);
            String name = "payment" + ".xlsx";

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" +
                    new String(
                            name.getBytes("UTF-8"),
                            "ISO-8859-1") +
                    "\";");

            BufferedInputStream bis = new BufferedInputStream(in);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            byte[] data = new byte[2048];
            int input = 0;
            while ((input = bis.read(data)) != -1) {
                bos.write(data, 0, input);
                bos.flush();
            }

            if (bos != null) bos.close();
            if (bis != null) bis.close();
            if (so != null) so.close();
            if (in != null) in.close();

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }

            if (!file.delete()) {
//                log.error(file.getName() + " 삭제에 실패했습니다.");
            } else {
//                log.info(file.getName() + " 삭제되었습니다.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    @Transactional
    public int member_delete(String member_seq) {
        adminDAO.update_outDate(member_seq);
        return adminDAO.member_delete(member_seq);
    }


    public void cellPic(String joined_campaign_seq, String img, Workbook workbook, Sheet sheet, int rowNum, int colNum) throws IOException {

        String filePath = uploadPath+"joined/"+joined_campaign_seq+"/"+img;
        InputStream is = new FileInputStream(filePath);
        byte[] bytes = IOUtils.toByteArray(is);
        int picIdx  = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_JPEG);
        is.close();

        XSSFCreationHelper helper = (XSSFCreationHelper) workbook.getCreationHelper();
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = helper.createClientAnchor();

        // 이미지 출력할 cell 위치
        anchor.setRow1(rowNum);
        anchor.setCol1(colNum);

        // 이미지 그리기
        XSSFPicture pic = drawing.createPicture(anchor, picIdx);
        pic.resize(2,1);
    }

    private static String followers(String followers) {
        if(followers!=null && !followers.equals("")) {
            return followers;
        }else {
            return "";
        }
    }

}
