package com.muzi.adtool.controller;

import com.muzi.adtool.dao.NoticeDAO;
import com.muzi.adtool.domain.BbsVO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.NoticeVO;
import com.muzi.adtool.domain.PageMaker;
import com.muzi.adtool.services.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class NoticeController {

    @Autowired
    NoticeDAO noticeDAO;

    @Autowired
    NoticeService noticeService;

    @PostMapping("/admin/api/notice/insert")
    @ResponseBody
    public int notice_insert_api(NoticeVO vo){
        return noticeService.insert(vo);
    }

    @GetMapping("/common/api/notice/list")
    @ResponseBody
    public HashMap<String, Object> notice_list_api(Criteria cri){
        HashMap<String, Object> data=new HashMap<>();
        cri.setPerPageNum(10);
        List<NoticeVO> list=noticeDAO.notice_list(cri);
        List<NoticeVO> highlightList=noticeDAO.highlight_list();
        // 내림차순
        int pageSize = cri.getPerPageNum();
        int pageNumber = cri.getPage()-1;
        int totalCount = noticeDAO.notice_totalCount(cri);
        int rowNum = pageSize * pageNumber;
        for(NoticeVO vo : list) {
            vo.setRowNum(totalCount-rowNum);
            rowNum++;
        }

        PageMaker pm=new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(totalCount);
        data.put("cri", cri);
        data.put("pm", pm);
        data.put("list", list);
        data.put("highlight", highlightList);
        return data;
    }

    @GetMapping("/common/api/notice/read")
    @ResponseBody
    public NoticeVO notice_read_api(String notice_seq){
        return noticeService.read(notice_seq);
    }

    @PutMapping("/admin/api/notice/update")
    @ResponseBody
    public int notice_update_api(NoticeVO vo){
        return noticeService.update_image_order(vo);
    }

    @PostMapping("/admin/api/notice/delete")
    @ResponseBody
    public int notice_delete(String notice_seq) {
        return noticeService.notice_delete(notice_seq);
    }
}
