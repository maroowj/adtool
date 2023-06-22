package com.muzi.adtool.dao;

import com.muzi.adtool.domain.BbsVO;
import com.muzi.adtool.domain.Criteria;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BbsDAOImpl implements BbsDAO{
    String namespace="BbsMapper";

    @Autowired
    SqlSession session;

    @Override
    public int bbs_insert(BbsVO vo) {
        return session.insert(namespace + ".bbs_insert", vo);
    }

    @Override
    public List<BbsVO> bbs_list(Criteria cri) {
        return session.selectList(namespace + ".bbs_list", cri);
    }

    @Override
    public int bbs_totalCount(Criteria cri) {
        return session.selectOne(namespace + ".bbs_totalCount", cri);
    }

    @Override
    public BbsVO bbs_read(String bbs_seq) {
        return session.selectOne(namespace + ".bbs_read", bbs_seq);
    }

    @Override
    public int readCount_update(String bbs_seq) {
        return session.update(namespace + ".readCount_update", bbs_seq);
    }

    @Override
    public int bbs_update(BbsVO vo) {
        return session.update(namespace + ".bbs_update", vo);
    }

    @Override
    public int bbs_update_image_order(BbsVO vo) {
        return session.update(namespace + ".bbs_update_image_order", vo);
    }

    @Override
    public List<BbsVO> highlight_list() {
        return session.selectList(namespace + ".highlight_list");
    }

    @Override
    public int bbs_delete(String bbs_seq) {
        return session.update(namespace + ".bbs_delete", bbs_seq);
    }


}
