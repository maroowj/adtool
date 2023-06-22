package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.JoinedVO;
import com.muzi.adtool.domain.Member_informationVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class JoinedDAOImpl implements JoinedDAO{

    String namespace = "JoinedMapper";

    @Autowired
    SqlSession session;

    @Override
    public int insert(JoinedVO vo) {
        return session.insert(namespace + ".insert", vo);
    }

    @Override
    public int chkCount(String campaign_seq) {
        return session.selectOne(namespace + ".chkCount", campaign_seq);
    }

    @Override
    public int chkDuplicate(JoinedVO vo) {
        return session.selectOne(namespace + ".chkDuplicate", vo);
    }

    @Override
    public List<JoinedVO> list_by_campaign_seq(String campaign_seq) {
        return session.selectList(namespace + ".list_by_campaign_seq", campaign_seq);
    }

    @Override
    public JoinedVO read_by_campaign_seq_and_member_seq(JoinedVO vo) {
        return session.selectOne(namespace + ".read_by_campaign_seq_and_member_seq", vo);
    }

    @Override
    public List<JoinedVO> list_by_member_seq_and_status(Criteria cri) {
        return session.selectList(namespace + ".list_by_member_seq_and_status", cri);
    }

    @Override
    public int totalCount_by_member_seq_and_status(Criteria cri) {
        return session.selectOne(namespace + ".totalCount_by_member_seq_and_status", cri);
    }

    @Override
    public int update_review_img(JoinedVO vo) {
        return session.update(namespace + ".update_review_img", vo);
    }

    @Override
    public int update_payment_img(JoinedVO vo) {
        return session.update(namespace + ".update_payment_img", vo);
    }

    @Override
    public int findByMemberAndBrand(String member_seq, String brand_seq) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("member_seq", member_seq);
        map.put("brand_seq", brand_seq);
        return session.selectOne(namespace + ".findByMemberAndBrand", map);
    }
}
