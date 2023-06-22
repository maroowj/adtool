package com.muzi.adtool.services;

import com.muzi.adtool.dao.CertificationDAO;
import com.muzi.adtool.domain.CertificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CertificationServiceImpl implements CertificationService{

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    CertificationDAO certificationDAO;

    @Override
    @Transactional
    public int insertCertification(CertificationVO vo) {
        vo.setCertification_seq(getSequenceService.getSeq("cert"));
        return certificationDAO.insertCertification(vo);
    }
}
