package com.muzi.adtool.services;

import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.MessageVO;
import com.muzi.adtool.domain.NoticeVO;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

@Service
public interface NoticeService {
    public int insert(NoticeVO vo);
    public NoticeVO read(String notice_seq);
    public int update_image_order(NoticeVO vo);

    public int notice_delete(String notice_seq);
}
