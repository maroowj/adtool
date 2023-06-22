package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.NoticeVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeDAOImpl implements NoticeDAO{
    String namespace="NoticeMapper";

    @Autowired
    SqlSession session;

    @Override
    public int notice_insert(NoticeVO vo) {
        return session.insert(namespace + ".notice_insert", vo);
    }

    @Override
    public List<NoticeVO> notice_list(Criteria cri) {
        return session.selectList(namespace + ".notice_list", cri);
    }

    @Override
    public int notice_totalCount(Criteria cri) {
        return session.selectOne(namespace + ".notice_totalCount", cri);
    }

    @Override
    public NoticeVO notice_read(String notice_seq) {
        return session.selectOne(namespace + ".notice_read", notice_seq);
    }

    @Override
    public int readCount_update(String notice_seq) {
        return session.update(namespace + ".readCount_update", notice_seq);
    }

    @Override
    public int notice_update(NoticeVO vo) {
        return session.update(namespace + ".notice_update", vo);
    }

    @Override
    public int notice_update_image_order(NoticeVO vo) {
        return session.update(namespace + ".notice_update_image_order", vo);
    }

    @Override
    public List<NoticeVO> highlight_list() {
        return session.selectList(namespace + ".highlight_list");
    }

    @Override
    public int notice_delete(String notice_seq) {
        return session.update(namespace + ".notice_delete", notice_seq);
    }


}
