package com.muzi.adtool.controller;

import com.muzi.adtool.dao.CampaignDAO;
import com.muzi.adtool.dao.JoinedDAO;
import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.JoinedVO;
import com.muzi.adtool.domain.PageMaker;
import com.muzi.adtool.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class CampaignController {

    @Resource(name="uploadPath")
    String path;

    @Autowired
    CampaignDAO campaignDAO;

    @Autowired
    CampaignService campaignService;

    @Autowired
    JoinedDAO joinedDAO;

    // 캠페인 생성
    @PostMapping("/admin/api/campaign/insert")
    @ResponseBody
    public int campaign_insert(CampaignVO vo){
//        System.out.println("vo="+vo.toString());
        return campaignService.insert(vo);
    }

    // 캠페인 삭제
    @DeleteMapping("/admin/api/campaign/delete")
    @ResponseBody
    public int campaign_delete(String campaign_seq){
        return campaignDAO.campaign_delete(campaign_seq);
    }

    @GetMapping("/common/api/campaign/read")
    @ResponseBody
    public HashMap<String, Object> campaignRead(String campaign_seq, String member_seq) {
        HashMap<String, Object> data = new HashMap<>();
        JoinedVO joinedVO = new JoinedVO();
        joinedVO.setCampaign_seq(campaign_seq);
        joinedVO.setMember_seq(member_seq);
        data.put("cvo", campaignDAO.campaign_read_by_seq(campaign_seq));
        data.put("jvo", joinedDAO.read_by_campaign_seq_and_member_seq(joinedVO));
        data.put("mList", joinedDAO.list_by_campaign_seq(campaign_seq));
        return data;
    }

    @GetMapping("/common/api/campaign/list")
    @ResponseBody
    public HashMap<String, Object> campaignList(Criteria cri){
        HashMap<String, Object> data = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();

        int totalCount = campaignDAO.totalCount(cri);
        if(cri.getStatus().equals("recruit")) {
            cri.setPerPageNum(totalCount);
        }else {
            cri.setPerPageNum(cri.getPerPageNum());
        }
        for(CampaignVO campaignVO:campaignDAO.campaign_all_list_by_country_and_status(cri)){
            String campaign_seq = campaignVO.getCampaign_seq();
            List<JoinedVO> jList = joinedDAO.list_by_campaign_seq(campaign_seq);
            HashMap<String, Object> map = new HashMap<>();
            map.put("campaignVO", campaignVO);
            map.put("jList", jList);
            list.add(map);
        }
        PageMaker pm = new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(totalCount);
        data.put("list", list);
        data.put("pm", pm);
        data.put("cri", cri);
        return data;
    }

    @PutMapping("/admin/api/campaign/update")
    @ResponseBody
    public int campaign_update_all(CampaignVO vo) {
        if (vo.getStatus() == null) {
//            System.out.println("vo="+vo.toString());
            return campaignService.update_image_order(vo);
        } else {
//            System.out.println(">>> status");
            return campaignDAO.campaign_status_update(vo);
        }
    }

    /************************************* 밑에는 일단 보류 *************************************/

    /*
    @GetMapping("/campaign/join2")
    public String joinView2(Model model, String campaign_seq){
        model.addAttribute("campaign_seq", campaign_seq);
        return "join_backUp2";
    }
        */


//    @GetMapping("/campaign/join_backUp")
//    public String joinBackUp(Model model, String campaign_seq, HttpSession session){
//        model.addAttribute("campaign_seq", campaign_seq);
//        model.addAttribute("cvo", campaignDAO.campaign_read_by_seq(campaign_seq));
//        model.addAttribute("memberList", joinedDAO.list_by_campaign_seq(campaign_seq));
//       JoinedVO joinedVO = new JoinedVO();
//       joinedVO.setCampaign_seq(campaign_seq);
//        joinedVO.setMember_seq((String)session.getAttribute("member_seq"));
////        //JoinedVO jvo = joinedDAO.read_by_campaign_seq_and_member_seq(joinedVO);
//        model.addAttribute("jvo", joinedDAO.read_by_campaign_seq_and_member_seq(joinedVO));
////        System.out.println(model.getAttribute("joinedVO"));
////        //System.out.println("jvo="+jvo.toString()); // joined_campaign 테이블에 seq가 없을 경우 null이 발생한다.
//        return "join_backUp";
//    }


    /*
    @PostMapping("/campaign/update")
    @ResponseBody
    public int campaign_update(CampaignVO vo, MultipartHttpServletRequest multi, String dir) throws Exception {
        CampaignVO oldVO = campaignDAO.campaign_read_by_seq(vo.getCampaign_seq());

        File target;
        MultipartFile file = multi.getFile("image1");
        if(file==null) {
            vo.setCampaign_img1(oldVO.getCampaign_img1());
        }else {
            String ext=FilenameUtils.getExtension(file.getOriginalFilename());
            String image = System.currentTimeMillis() + "." + ext;
            File folder = new File(path+dir);
            if(!folder.exists()) folder.mkdirs();
            target = new File(path+dir, image);
            FileCopyUtils.copy(file.getBytes(), target);
            vo.setCampaign_img1(image);

            if(oldVO.getCampaign_img1()!=null) {
                new File(path + dir + "/" + oldVO.getCampaign_img1()).delete();
            }
        }

        file = multi.getFile("image2");
        if(file==null) {
            vo.setCampaign_img2(oldVO.getCampaign_img2());
        }else {
            String ext=FilenameUtils.getExtension(file.getOriginalFilename());
            String image = System.currentTimeMillis() + "." + ext;
            File folder = new File(path+dir);
            if(!folder.exists()) folder.mkdirs();
            target = new File(path+dir, image);
            FileCopyUtils.copy(file.getBytes(), target);
            vo.setCampaign_img2(image);

            if(oldVO.getCampaign_img2()!=null) {
                new File(path + dir + "/" + oldVO.getCampaign_img2()).delete();
            }
        }

        file = multi.getFile("image3");
        if(file==null) {
            vo.setCampaign_img3(oldVO.getCampaign_img3());
        }else {
            String ext=FilenameUtils.getExtension(file.getOriginalFilename());
            String image = System.currentTimeMillis() + "." + ext;
            File folder = new File(path+dir);
            if(!folder.exists()) folder.mkdirs();
            target = new File(path+dir, image);
            FileCopyUtils.copy(file.getBytes(), target);
            vo.setCampaign_img3(image);

            if(oldVO.getCampaign_img3()!=null) {
                new File(path + dir + "/" + oldVO.getCampaign_img3()).delete();
            }
        }

        file = multi.getFile("image4");
        if(file==null) {
            vo.setCampaign_img4(oldVO.getCampaign_img4());
        }else {
            String ext=FilenameUtils.getExtension(file.getOriginalFilename());
            String image = System.currentTimeMillis() + "." + ext;
            File folder = new File(path+dir);
            if(!folder.exists()) folder.mkdirs();
            target = new File(path+dir, image);
            FileCopyUtils.copy(file.getBytes(), target);
            vo.setCampaign_img4(image);

            if(oldVO.getCampaign_img4()!=null) {
                new File(path + dir + "/" + oldVO.getCampaign_img4()).delete();
            }
        }

        file = multi.getFile("image5");
        if(file==null) {
            vo.setCampaign_img5(oldVO.getCampaign_img5());
        }else {
            String ext=FilenameUtils.getExtension(file.getOriginalFilename());
            String image = System.currentTimeMillis() + "." + ext;
            File folder = new File(path+dir);
            if(!folder.exists()) folder.mkdirs();
            target = new File(path+dir, image);
            FileCopyUtils.copy(file.getBytes(), target);
            vo.setCampaign_img5(image);

            if(oldVO.getCampaign_img5()!=null) {
                new File(path + dir + "/" + oldVO.getCampaign_img5()).delete();
            }
        }
        System.out.println("vo= "+vo.toString());

        if(vo.getStatus()==null){
            return campaignDAO.campaign_update(vo);
        }else{
            return campaignDAO.campaign_status_update(vo);
        }
    }
     */
}
