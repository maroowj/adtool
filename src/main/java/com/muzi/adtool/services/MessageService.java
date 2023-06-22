package com.muzi.adtool.services;

import com.muzi.adtool.domain.MessageVO;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    public int insert(MessageVO vo);
}
