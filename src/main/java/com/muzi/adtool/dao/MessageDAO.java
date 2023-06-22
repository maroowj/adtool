package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.MessageVO;

import java.util.List;

public interface MessageDAO {

    public int insert_message(MessageVO vo);
    public List<MessageVO> message_list_by_member_seq(Criteria cri);
    public int message_totalCount_by_member_seq(Criteria cri);
    public int message_count_for_notRead_by_member_seq(String member_seq);

    public int update_read_check(String message_seq);
    public MessageVO message_read_by_message_seq(String message_seq, String member_seq);

    public List<MessageVO> message_list_all(Criteria cri);
    public int message_totalCount_all(Criteria cri);
    public MessageVO message_read(String message_seq);
}
