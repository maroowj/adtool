package com.muzi.adtool.controller;

import com.muzi.adtool.dao.CampaignDAO;
import com.muzi.adtool.dao.JoinedDAO;
import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.*;
import com.muzi.adtool.services.JoinedService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class JoinController {

    @Resource(name="uploadPath")
    String path;

    @Autowired
    JoinedDAO joinedDAO;

    @Autowired
    JoinedService joinedService;

    @Autowired
    CampaignDAO campaignDAO;

    @Autowired
    MemberDAO memberDAO;

    @PostMapping("/common/api/joined_campaign/insert")
    @ResponseBody
    public int joinCampaign(JoinedVO vo){
        return joinedService.insert(vo);
    }

    @GetMapping("/common/api/joined_campaign/read/status_check")
    @ResponseBody
    public int chkDuplicate(JoinedVO vo){
        //System.out.println("vo="+vo.toString());
        int result = 0;
        CampaignVO campaignVO = campaignDAO.campaign_read_by_seq(vo.getCampaign_seq());
        int multiple = campaignVO.getMultiple_number();
        int chkCount = joinedDAO.chkCount(vo.getCampaign_seq());
        //System.out.println("chkCount="+chkCount);
        int chkDuplicate = joinedDAO.chkDuplicate(vo);
        //System.out.println("chkDuplicate= "+chkDuplicate);
        JoinedVO joinedVO = joinedDAO.read_by_campaign_seq_and_member_seq(vo);
        //System.out.println("joinedVO="+joinedVO);
        if(campaignVO.getStatus().equals("progress") || campaignVO.getStatus().equals("recruit")){
            if(chkCount >= multiple) {
                result=0; // 신청 인원이 만원일 때,
                if(joinedVO!=null && joinedVO.getAccept().equals("accept")){
                    result=3; // 승인 되었을 때
                }else if(joinedVO!=null && joinedVO.getAccept().equals("decline")) {
                    result = 4; // 거절 되었을 때
                }
            }else {
                if(chkDuplicate >= 1){
                    result=1; // 이미 회원이 신청 했을 때,
                    if(joinedVO.getAccept().equals("accept")){
                        result=3; // 승인 되었을 때
                    }else if(joinedVO.getAccept().equals("decline")) {
                        result = 4; // 거절 되었을 때
                    }
                }else{
                    if(campaignVO.getStatus().equals("progress")){
                        result=5;
                    }else{
                        result=2; // 신청하지 않았을 때, (신청 가능)
                    }
                }
            }
        }else if(campaignVO.getStatus().equals("end")) {
            result=5; // 종료된 캠페인
        }
        //System.out.println(result);
        return result;
    }

    @GetMapping("/user/api/joined_campaign/list")
    @ResponseBody
    public HashMap<String, Object> list_by_member_seq_and_status(Criteria cri){
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        cri.setPerPageNum(10);
        for(JoinedVO joinedVO:joinedDAO.list_by_member_seq_and_status(cri)){
            String campaign_seq=joinedVO.getCampaign_seq();
            CampaignVO campaignVO = campaignDAO.campaign_read_by_seq(campaign_seq);
            HashMap<String, Object> map = new HashMap<>();
            map.put("joinedVO", joinedVO);
            map.put("campaignVO", campaignVO);
            list.add(map);
        }
        PageMaker pm=new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(joinedDAO.totalCount_by_member_seq_and_status(cri));
        data.put("list", list);
        data.put("cri", cri);
        data.put("pm", pm);
        return data;
    }


    @PostMapping("/user/api/joined_campaign/update/image")
    public String campaign_update(MultipartHttpServletRequest multi) throws Exception {
        JoinedVO vo = new JoinedVO();
        vo.setCampaign_seq(multi.getParameter("campaign_seq"));
        vo.setMember_seq(multi.getParameter("member_seq"));

        JoinedVO oldVO = joinedDAO.read_by_campaign_seq_and_member_seq(vo);
        String joined_campaign_seq= oldVO.getJoined_campaign_seq();
        vo.setJoined_campaign_seq(joined_campaign_seq);

        File target;
        String dir=multi.getParameter("dir");
        String type= multi.getParameter("img_type");

        if(type.equals("payment")){ // 이미지 타입이 payment일 때,
            MultipartFile payment_img1 = multi.getFile("payment_img1");
            if(payment_img1.getOriginalFilename().equals("")) {
                vo.setPayment_img1(oldVO.getPayment_img1());
            }else {
                String ext= FilenameUtils.getExtension(payment_img1.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(payment_img1.getBytes(), target);
                vo.setPayment_img1(image);

                if(oldVO.getPayment_img1()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getPayment_img1()).delete();
                }
            }

            MultipartFile payment_img2 = multi.getFile("payment_img2");
//            System.out.println("img2="+payment_img2.getOriginalFilename());
            if(payment_img2.getOriginalFilename().equals("")) {
//                System.out.println("img2_nullChk="+payment_img2);
                vo.setPayment_img2(oldVO.getPayment_img2());
            }else {
                String ext= FilenameUtils.getExtension(payment_img2.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(payment_img2.getBytes(), target);
                vo.setPayment_img2(image);

                if(oldVO.getPayment_img2()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getPayment_img2()).delete();
                }
            }

            MultipartFile payment_img3 = multi.getFile("payment_img3");
            if(payment_img3.getOriginalFilename().equals("")) {
                vo.setPayment_img3(oldVO.getPayment_img3());
            }else {
                String ext= FilenameUtils.getExtension(payment_img3.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(payment_img3.getBytes(), target);
                vo.setPayment_img3(image);

                if(oldVO.getPayment_img3()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getPayment_img3()).delete();
                }
            }

            MultipartFile payment_img4 = multi.getFile("payment_img4");
            if(payment_img4.getOriginalFilename().equals("")) {
                vo.setPayment_img4(oldVO.getPayment_img4());
            }else {
                String ext= FilenameUtils.getExtension(payment_img4.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(payment_img4.getBytes(), target);
                vo.setPayment_img4(image);

                if(oldVO.getPayment_img4()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getPayment_img4()).delete();
                }
            }
            joinedDAO.update_payment_img(vo);
        }
        else if(type.equals("review")){ // 이미지 타입이 review일 때,
            MultipartFile review_img1 = multi.getFile("review_img1");
            if(review_img1.getOriginalFilename().equals("")) {
                vo.setReview_img1(oldVO.getReview_img1());
            }else {
                String ext= FilenameUtils.getExtension(review_img1.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(review_img1.getBytes(), target);
                vo.setReview_img1(image);

                if(oldVO.getReview_img1()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getReview_img1()).delete();
                }
            }

            MultipartFile review_img2 = multi.getFile("review_img2");
            if(review_img2.getOriginalFilename().equals("")) {
                vo.setReview_img2(oldVO.getReview_img2());
            }else {
                String ext= FilenameUtils.getExtension(review_img2.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(review_img2.getBytes(), target);
                vo.setReview_img2(image);

                if(oldVO.getReview_img2()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getReview_img2()).delete();
                }
            }

            MultipartFile review_img3 = multi.getFile("review_img3");
            if(review_img3.getOriginalFilename().equals("")) {
                vo.setReview_img3(oldVO.getReview_img3());
            }else {
                String ext= FilenameUtils.getExtension(review_img3.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(review_img3.getBytes(), target);
                vo.setReview_img3(image);

                if(oldVO.getReview_img1()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getReview_img3()).delete();
                }
            }

            MultipartFile review_img4 = multi.getFile("review_img4");
            if(review_img4.getOriginalFilename().equals("")) {
                vo.setReview_img4(oldVO.getReview_img4());
            }else {
                String ext= FilenameUtils.getExtension(review_img4.getOriginalFilename());
                String image = System.currentTimeMillis() + "." + ext;
                File folder = new File(path+dir+joined_campaign_seq);
                if(!folder.exists()) folder.mkdirs();
                target = new File(path+dir+joined_campaign_seq, image);
                FileCopyUtils.copy(review_img4.getBytes(), target);
                vo.setReview_img4(image);

                if(oldVO.getReview_img4()!=null) {
                    new File(path + dir + joined_campaign_seq + "/" + oldVO.getReview_img4()).delete();
                }
            }

            joinedDAO.update_review_img(vo);
        }

        //System.out.println("vo="+ vo.toString());
        return "redirect:/campaign/join?campaign_seq="+vo.getCampaign_seq();
    }


}
