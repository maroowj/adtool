package com.muzi.adtool.controller;

import com.muzi.adtool.dao.BbsDAO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.BbsVO;
import com.muzi.adtool.domain.Member_informationVO;
import com.muzi.adtool.domain.PageMaker;
import com.muzi.adtool.services.BbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class BbsController {

    @Autowired
    BbsDAO bbsDAO;

    @Autowired
    BbsService bbsService;

    @PostMapping("/admin/api/help/insert")
    @ResponseBody
    public int notice_insert_api(BbsVO vo){
        return bbsService.insert(vo);
    }

    @GetMapping("/common/api/help/list")
    @ResponseBody
    public HashMap<String, Object> notice_list_api(Criteria cri){
        HashMap<String, Object> data=new HashMap<>();
        cri.setPerPageNum(10);
        List<BbsVO> list=bbsDAO.bbs_list(cri);

        // 내림차순
        int pageSize = cri.getPerPageNum();
        int pageNumber = cri.getPage()-1;
        int totalCount = bbsDAO.bbs_totalCount(cri);
        int rowNum = pageSize * pageNumber;
        for(BbsVO vo : list) {
            vo.setRowNum(totalCount-rowNum);
            rowNum++;
        }

        PageMaker pm=new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(totalCount);
        data.put("cri", cri);
        data.put("pm", pm);
        data.put("list", list);
        return data;
    }

    @GetMapping("/common/api/help/read")
    @ResponseBody
    public BbsVO bbs_read_api(String bbs_seq){
        return bbsService.read(bbs_seq);
    }

    @PutMapping("/admin/api/help/update")
    @ResponseBody
    public int notice_update_api(BbsVO vo){
        return bbsService.update_image_order(vo);
    }

    @PostMapping("/admin/api/help/delete")
    @ResponseBody
    public int notice_delete(String bbs_seq) {
        return bbsService.notice_delete(bbs_seq);
    }
}
