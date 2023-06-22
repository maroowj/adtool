package com.muzi.adtool.dao;

import com.muzi.adtool.domain.*;

import java.util.List;

public interface NoticeDAO {
    public int notice_insert(NoticeVO vo);
    public List<NoticeVO> notice_list(Criteria cri);
    public int notice_totalCount(Criteria cri);
    public NoticeVO notice_read(String notice_seq);
    public int readCount_update(String notice_seq);
    public int notice_update(NoticeVO vo);
    public int notice_update_image_order(NoticeVO vo);

    public List<NoticeVO> highlight_list();
    public int notice_delete(String notice_seq);
}
