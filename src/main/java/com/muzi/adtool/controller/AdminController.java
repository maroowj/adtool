package com.muzi.adtool.controller;

import com.muzi.adtool.dao.*;
import com.muzi.adtool.domain.*;
import com.muzi.adtool.services.AdminService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    AdminDAO adao;

    @Autowired
    AdminService adminService;

    @Autowired
    CampaignDAO cdao;

    @Autowired
    JoinedDAO jdao;

    @Autowired
    BrandDAO bdao;

    @Autowired
    MessageDAO mdao;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 브랜드 리스트 및 하위 캠페인 리스트
    @GetMapping("/admin/api/index/list")
    @ResponseBody
    public HashMap<String, Object> adminIndexList(Criteria cri){
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        String searchType=cri.getSearchType();
        String keyword=cri.getKeyword();
        for(CampaignVO bvo:adao.brand_list_by_country_for_admin(cri)){
            HashMap<String, Object> map = new HashMap<>();
            //System.out.println(">>>"+cri.getSearchType());
            //System.out.println(">>>"+cri.getKeyword());
            String brand_seq=bvo.getBrand_seq();
            cri.setBrand_seq(brand_seq);
            cri.setSearchType(searchType);
            cri.setKeyword(keyword);
            List<CampaignVO> cList=adao.campaign_list_by_brand_seq_and_country(cri);
            CampaignVO firstCampaign=adao.firstCampaign_by_brand(brand_seq);
//            String thumbnail = cList.get(0).getCampaign_img1();
//            System.out.println(">>>"+thumbnail);
//            if(thumbnail==null || thumbnail.equals("")){
//                thumbnail=cList.get(1).getCampaign_img1();
//            }
            map.put("thumbnail", firstCampaign.getCampaign_img1());
            map.put("bvo", bvo);
            HashMap<String, Object> cm = new HashMap<>();
            List<HashMap<String, Object>> cl = new ArrayList<>();
            for(CampaignVO cvo:cList){
                HashMap<String, Object> cmap = new HashMap<>();
                String campaign_seq=cvo.getCampaign_seq();
                cvo.setApply_count(jdao.chkCount(campaign_seq));
                cmap.put("cvo", cvo);
                cl.add(cmap);
            }
            map.put("cl", cl);
            list.add(map);
        }
        
        data.put("list", list);
        return data;
    }

    /***************************** 캠페인 *****************************/

    // 캠페인 신청 명단
    @GetMapping("/admin/api/joined_campaign/list")
    @ResponseBody
    public List<PaymentVO> joined_member_list(String campaign_seq){
        return adao.joined_member_list_by_campaign_seq(campaign_seq);
    }

    // 캠페인 명단의 회원 정보 수정
    @PutMapping("/admin/api/joined_campaign/update/member_info")
    @ResponseBody
    public int update_joined_member_info(JoinedVO vo){
        return adminService.joined_member_list_update(vo);
    }

    // 캠페인 명단의 회원 승인/탈락 변경 (Update)
    @PatchMapping("/admin/api/joined_campaign/update/member_accept")
    @ResponseBody
    public int update_joined_member_accept(PaymentVO vo){
        return adminService.status_update_and_payment_insert(vo);
    }

    // 캠페인 리뷰 확인 Update
    @PatchMapping("/admin/api/joined_campaign/update/review_check")
    @ResponseBody
    public int update_review_check(JoinedVO vo){
        return adao.review_check_update(vo);
    }

    // 캠페인 정산 list
    @GetMapping("/admin/api/payment/list")
    @ResponseBody
    public List<PaymentVO> payment_list_api(String campaign_seq){
        return adao.payment_list_by_campaign_seq(campaign_seq);
    }

    // 캠페인 정산 Update
    @PutMapping("/admin/api/payment/update")
    @ResponseBody
    public int admin_payment_update(PaymentVO vo){
        return adao.payment_update(vo);
    }

    /****************************** 회원 ******************************/

    @GetMapping("/admin/api/member/list")
    @ResponseBody
    public HashMap<String, Object> member_all_list(Criteria cri) {
        HashMap<String, Object> data=new HashMap<>();
        cri.setDesc("desc");
        List<Member_informationVO> list = adao.member_all_list_except_for_admin(cri);
        //오름차순
//        int pageSize = cri.getPerPageNum();
//        int pageNumber = cri.getPage()-1;
//        int rowNum = (pageSize * pageNumber) + 1;
//        for(Member_informationVO ivo : list) {
//            ivo.setRowNum(rowNum);
//            rowNum++;
//        }
        // 내림차순
        int pageSize = cri.getPerPageNum();
        int pageNumber = cri.getPage()-1;
        int totalCount = adao.member_all_count_except_for_admin(cri);
        int rowNum = pageSize * pageNumber;
        for(Member_informationVO ivo : list) {
            ivo.setRowNum(totalCount-rowNum);
            rowNum++;
        }

        PageMaker pm = new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(totalCount);
        data.put("list", list);
        data.put("pm", pm);
        data.put("cri", cri);
        return data;
    }

    @PutMapping("/admin/api/member/update")
    @ResponseBody
    public int update_member_info(Member_informationVO vo){
        return adao.member_info_update(vo);
    }

    // 회원 비밀번호 초기화
    @PatchMapping("/admin/api/member/default")
    @ResponseBody
    public int password_default(MemberVO memberVO) {
        memberVO.setMember_password(passwordEncoder.encode("00000000"));
        return adao.password_default(memberVO);
    }

    // 회원 삭제(탈퇴 처리)
    @DeleteMapping("/admin/api/member/delete")
    @ResponseBody
    public int member_delete(String member_seq) {
        return adminService.member_delete(member_seq);
    }

    /***************************** 쪽지함 *****************************/

    @GetMapping("/admin/api/message/list")
    @ResponseBody
    public HashMap<String, Object> message_list_all(Criteria cri){
        HashMap<String, Object> data=new HashMap<>();
        cri.setPerPageNum(10);
        List<MessageVO> list=mdao.message_list_all(cri);
        PageMaker pm=new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(mdao.message_totalCount_all(cri));
        data.put("list", list);
        data.put("pm", pm);
        data.put("cri", cri);
        return data;
    }

    @GetMapping("/admin/api/message/read")
    @ResponseBody
    public MessageVO message_read(String message_seq){
        return mdao.message_read(message_seq);
    }


    /***************************** 예약 캠페인 *****************************/

    // 에약 캠페인의 브랜드 리스트 및 하위 캠페인 리스트

    @GetMapping("/admin/api/reservation_campaign/list")
    @ResponseBody
    public HashMap<String, Object> reservation_campaign_list(Criteria cri){
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();

        String searchType=cri.getSearchType();
        String keyword=cri.getKeyword();

        for(BrandVO bvo:adao.brand_list_by_country_in_reservation_campaign(cri)){
            HashMap<String, Object> map = new HashMap<>();
//            System.out.println(bvo.toString());
            //System.out.println(">>>"+cri.getSearchType());
            //System.out.println(">>>"+cri.getKeyword());
            String brand_seq=bvo.getBrand_seq();
            cri.setBrand_seq(brand_seq);
            cri.setSearchType(searchType);
            cri.setKeyword(keyword);
            List<Reservation_campaignVO> rList=adao.reservation_campaign_list_by_brand_seq_and_country(cri);
            map.put("bvo", bvo);
            String thumbnail = rList.get(0).getCampaign_img1();
//            System.out.println(">>>"+thumbnail);
//            if(thumbnail==null || thumbnail.equals("")){
//                thumbnail=rList.get(1).getCampaign_img1();
//            }
            map.put("thumbnail", thumbnail);
            List<HashMap<String, Object>> rl = new ArrayList<>();
            for(Reservation_campaignVO rvo:rList){
                HashMap<String, Object> cmap = new HashMap<>();
                cmap.put("rvo", rvo);
                rl.add(cmap);
            }
            map.put("rl", rl);
            list.add(map);
        }
        data.put("list", list);
        return data;
    }

    @PostMapping("/admin/api/reservation_campaign/insert")
    @ResponseBody
    public int reservation_campaign_insert(Reservation_campaignVO vo){
//        System.out.println(vo.toString());
        return adminService.reservation_campaign_insert(vo);
    }

    @GetMapping("/admin/api/reservation_campaign/read")
    @ResponseBody
    public Reservation_campaignVO reservationCampaignRead(String re_campaign_seq) {
        return adao.reservationCampaignRead(re_campaign_seq);
    }

    @DeleteMapping("/admin/api/reservation_campaign/delete")
    @ResponseBody
    public void reservationCampaignDeleteBySeq(String re_campaign_seq) {
        adao.reservationCampaignDeleteBySeq(re_campaign_seq);
    }

    /***************************** 포인트 *****************************/

    // 220608 친구추천 포인트 적립
    @PostMapping("/admin/api/point/recommend")
    @ResponseBody
    public int insert_point_by_recommender(PointVO vo) {
        return adminService.insert_point_by_recommender(vo);
    }

    // 220608 전체 회원 포인트 전체 내역 목록 조회
    @GetMapping("/admin/api/point/list")
    @ResponseBody
    public Map<String, Object> all_list_point(Criteria cri) {
        Map<String, Object> data = new HashMap<>();
        List<PointVO> list = adao.all_list_point(cri);
        PageMaker pm=new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(adao.totalCount_point(cri));
        data.put("cri", cri);
        data.put("pm", pm);
        data.put("list", list);

        return data;
    }

    // 220609 캠페인 정산 페이지 내 포인트 적립 및 회수
    @PostMapping("/admin/api/point/campaign")
    @ResponseBody
    public int insert_point_by_campaign(PointVO vo) {
        if(vo.getType().equals("save")) {
            return adminService.insert_point_by_campaign(vo);
        }else {
            return adminService.withdraw_point_by_done(vo);
        }
    }

    // 220609 출금(환급) 요청 처리
    @PostMapping("/admin/api/point/exchange")
    @ResponseBody
    public int update_point_for_exchange(PointVO vo) {

        if(vo.getStatus()==1) {
            vo.setDetail("사용자 출금 요청 (Complete)");
            vo.setType("출금");
        }else {
            vo.setDetail("사용자 출금 요청");
            vo.setType("요청");
        }

        return adao.update_point_for_exchange(vo);
    }


    /***************************** 엑셀다운로드 *****************************/

    // 가입회원관리 엑셀다운로드
    @GetMapping("/admin/api/member/list/excel")
    public void memberExcelDownload(Criteria cri, HttpServletResponse response) {
        adminService.memberExcelDownload(cri, response);
    }

    // 캠페인 참가자 명단 엑셀다운로드
    @GetMapping("/admin/api/joined_campaign/list/excel")
    public void joinedMemberExcelDownload(String campaign_seq, HttpServletResponse response) {
        adminService.joinedMemberExcelDownload(campaign_seq, response);
    }

    // 캠페인 리뷰 스크린샷 엑셀 다운로드
    @GetMapping("/admin/api/joined_campaign/review/excel")
    public void campaignReviewExcelDownload(String campaign_seq, HttpServletResponse response) throws IOException {
        adminService.campaignReviewExcel(campaign_seq, response);
    }

    // 캠페인 정산 스크린샷 엑셀 다운로드
    @GetMapping("/admin/api/joined_campaign/calculate/excel")
    public void campaignCalculateExcelDownload(String campaign_seq, HttpServletResponse response) throws IOException {
        adminService.campaignCalExcel(campaign_seq, response);
    }

    /***************************** 블랙 리스트 설정 *****************************/
    @PatchMapping("/admin/api/member/banned")
    @ResponseBody
    public void banned_member(Member_informationVO vo) {
        if(vo.getBanned()==0) {
            vo.setBanned_date(null);
        }else {
            vo.setBanned_date(new Date());
        }
        adao.member_banned(vo);
    }
}
