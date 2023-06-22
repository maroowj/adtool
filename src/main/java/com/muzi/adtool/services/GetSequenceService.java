package com.muzi.adtool.services;

import com.muzi.adtool.domain.BrandVO;
import org.springframework.stereotype.Service;

@Service
public interface GetSequenceService {
    public String getSeq(String seq_name);
}
