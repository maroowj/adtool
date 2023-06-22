package com.muzi.adtool.dao;

import com.muzi.adtool.domain.CertificationVO;

public interface CertificationDAO {

    public CertificationVO findByCode(String certification_code);
    public int insertCertification(CertificationVO vo);
    public int updateCertification(String certification_seq);
}
