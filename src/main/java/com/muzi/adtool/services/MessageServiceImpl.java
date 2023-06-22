package com.muzi.adtool.services;

import com.muzi.adtool.dao.MessageDAO;
import com.muzi.adtool.domain.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MessageServiceImpl implements MessageService{

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    MessageDAO messageDAO;

    @Transactional
    @Override
    public int insert(MessageVO vo) {
        vo.setMessage_seq(getSequenceService.getSeq("mssg"));
        vo.setSender("mber_0000000001");
        return messageDAO.insert_message(vo);
    }
}
