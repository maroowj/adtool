package com.muzi.adtool.controller;

import com.muzi.adtool.dao.MessageDAO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.MessageVO;
import com.muzi.adtool.domain.PageMaker;
import com.muzi.adtool.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageDAO mdao;

    @Autowired
    MessageService messageService;

    @PostMapping("/admin/api/message/insert")
    @ResponseBody
    public int message_insert(MessageVO vo){
        return messageService.insert(vo);
    }

    // 유저의 마이페이지에서 확인하는 쪽지 리스트
    @GetMapping("/user/api/message/list")
    @ResponseBody
    public HashMap<String, Object> message_list_by_member_seq(Criteria cri){
        HashMap<String, Object> data=new HashMap<>();
        cri.setPerPageNum(10);
        List<MessageVO> list=mdao.message_list_by_member_seq(cri);
        PageMaker pm=new PageMaker();
        pm.setCri(cri);
        pm.setTotalCount(mdao.message_totalCount_by_member_seq(cri));
        data.put("list", list);
        data.put("pm", pm);
        data.put("cri", cri);
        return data;
    }

    // 쪽지 내용 읽기
     @GetMapping("/user/api/message/read")
     @ResponseBody
     public MessageVO message_read_by_message_seq(String message_seq, String member_seq){
        return mdao.message_read_by_message_seq(message_seq, member_seq);
     }

    // 읽음 확인 상태 변경(update)
    @PatchMapping("/user/api/message/update/read_check")
    @ResponseBody
    public int update_read_check(String message_seq){
        return mdao.update_read_check(message_seq);
    }

   // 읽지 않은 메시지 개수 (Get)
   @GetMapping("/user/api/message/read/not_read_count")
   @ResponseBody
   public int message_count_for_notRead_by_member_seq(String member_seq){
        return mdao.message_count_for_notRead_by_member_seq(member_seq);
   }
}
