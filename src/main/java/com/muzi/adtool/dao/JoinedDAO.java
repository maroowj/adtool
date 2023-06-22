package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.JoinedVO;
import com.muzi.adtool.domain.Member_informationVO;

import java.util.HashMap;
import java.util.List;

public interface JoinedDAO {

    public int insert(JoinedVO vo);
    public int chkCount(String campaign_seq);
    public int chkDuplicate(JoinedVO vo);
    public List<JoinedVO> list_by_campaign_seq(String campaign_seq);
    public JoinedVO read_by_campaign_seq_and_member_seq(JoinedVO vo);
    public List<JoinedVO> list_by_member_seq_and_status(Criteria cri);
    public int totalCount_by_member_seq_and_status(Criteria cri);

    public int update_review_img(JoinedVO vo);
    public int update_payment_img(JoinedVO vo);

    public int findByMemberAndBrand(String member_seq, String brand_seq);
}
