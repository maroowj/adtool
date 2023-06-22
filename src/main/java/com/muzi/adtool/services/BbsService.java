package com.muzi.adtool.services;

import com.muzi.adtool.domain.BbsVO;
import com.muzi.adtool.domain.NoticeVO;
import org.springframework.stereotype.Service;

@Service
public interface BbsService {
    public int insert(BbsVO vo);
    public BbsVO read(String bbs_seq);
    public int update_image_order(BbsVO vo);

    public int notice_delete(String bbs_seq);
}
