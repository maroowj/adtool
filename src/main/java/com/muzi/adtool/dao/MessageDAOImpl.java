package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.MessageVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class MessageDAOImpl implements MessageDAO {

    String namespace="MessageMapper";

    @Autowired
    SqlSession session;

    @Override
    public int insert_message(MessageVO vo) {
        return session.insert(namespace + ".insert_message", vo);
    }

    @Override
    public List<MessageVO> message_list_by_member_seq(Criteria cri) {
        return session.selectList(namespace + ".message_list_by_member_seq", cri);
    }

    @Override
    public int message_totalCount_by_member_seq(Criteria cri) {
        return session.selectOne(namespace + ".message_totalCount_by_member_seq", cri);
    }

    @Override
    public int message_count_for_notRead_by_member_seq(String member_seq) {
        return session.selectOne(namespace + ".message_count_for_notRead_by_member_seq", member_seq);
    }

    @Override
    public int update_read_check(String message_seq) {
        return session.update(namespace + ".update_read_check", message_seq);
    }

    @Override
    public MessageVO message_read_by_message_seq(String message_seq, String member_seq) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("message_seq", message_seq);
        map.put("member_seq", member_seq);
        return session.selectOne(namespace + ".message_read_by_message_seq", map);
    }

    @Override
    public List<MessageVO> message_list_all(Criteria cri) {
        return session.selectList(namespace + ".message_list_all", cri);
    }

    @Override
    public int message_totalCount_all(Criteria cri) {
        return session.selectOne(namespace + ".totalCount", cri);
    }

    @Override
    public MessageVO message_read(String message_seq) {
        return session.selectOne(namespace + ".message_read", message_seq);
    }
}
