package com.muzi.adtool.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GetSequenceDAOImpl implements GetSequenceDAO{

    String namespace = "GetSequence";

    @Autowired
    SqlSession session;

    @Override
    public String getSeq(String seq_name) {
        return session.selectOne(namespace + ".getSeq", seq_name);
    }

    @Override
    public String getSeq2(String seq_name) {
        return session.selectOne(namespace + ".getSeq2", seq_name);
    }

    @Override
    public void update(String seq_name) {
        session.update(namespace + ".seq_update", seq_name);
    }

    @Override
    public void insert(String seq_name) {
        session.insert(namespace + ".seq_insert", seq_name);
    }
}
