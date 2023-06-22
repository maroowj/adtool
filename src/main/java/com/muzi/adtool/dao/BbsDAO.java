package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.BbsVO;

import java.util.List;

public interface BbsDAO {
    public int bbs_insert(BbsVO vo);
    public List<BbsVO> bbs_list(Criteria cri);
    public int bbs_totalCount(Criteria cri);
    public BbsVO bbs_read(String bbs_seq);
    public int readCount_update(String bbs_seq);
    public int bbs_update(BbsVO vo);
    public int bbs_update_image_order(BbsVO vo);

    public List<BbsVO> highlight_list();
    public int bbs_delete(String bbs_seq);
}
