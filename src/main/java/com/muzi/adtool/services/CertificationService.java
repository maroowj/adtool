package com.muzi.adtool.services;

import com.muzi.adtool.domain.CertificationVO;
import org.springframework.stereotype.Service;

@Service
public interface CertificationService {

    public int insertCertification(CertificationVO vo);
}
