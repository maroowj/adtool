package com.muzi.adtool.services;

import com.muzi.adtool.domain.JoinedVO;
import org.springframework.stereotype.Service;

@Service
public interface JoinedService {
    public int insert(JoinedVO vo);
}
