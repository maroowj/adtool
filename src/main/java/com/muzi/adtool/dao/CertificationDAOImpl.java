package com.muzi.adtool.dao;

import com.muzi.adtool.domain.CertificationVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CertificationDAOImpl implements CertificationDAO{

    String namespace = "CertificationMapper";

    @Autowired
    SqlSession session;


    @Override
    public CertificationVO findByCode(String certification_code) {
        return session.selectOne(namespace + ".findByCertificationCode", certification_code);
    }

    @Override
    public int insertCertification(CertificationVO vo) {
        return session.insert(namespace + ".insertCertification", vo);
    }

    @Override
    public int updateCertification(String certification_seq) {
        return session.update(namespace + ".updateCertification", certification_seq);
    }
}
