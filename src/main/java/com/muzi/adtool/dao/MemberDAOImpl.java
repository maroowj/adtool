package com.muzi.adtool.dao;

import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.MemberVO;
import com.muzi.adtool.domain.Member_informationVO;
import com.muzi.adtool.domain.PointVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDAOImpl implements MemberDAO{

    String namespace = "MemberMapper";

    @Autowired
    SqlSession session;

    @Override
    public int insert(MemberVO vo) {
        return session.insert(namespace + ".insert", vo);
    }

    @Override
    public int insertType(MemberVO vo) {
        return session.insert(namespace + ".insertType", vo);
    }

    @Override
    public int insertInfo(Member_informationVO vo) {
        return session.insert(namespace + ".insertInfo", vo);
    }

    @Override
    public MemberVO findByMemberId(String memberId) {
        return session.selectOne(namespace + ".findByMemberId", memberId);
    }

    @Override
    public List<Member_informationVO> infoList() {
        return session.selectList(namespace + ".infoList");
    }

    @Override
    public Member_informationVO infoReadByMemberSeq(String member_seq) {
        return session.selectOne(namespace + ".infoReadByMemberSeq", member_seq);
    }

    @Override
    public int updatePreferencesOfInformation(Member_informationVO vo) {
        return session.update(namespace + ".updatePreferencesOfInformation", vo);
    }

    @Override
    public int updateSnsOfInformation(Member_informationVO vo) {
        return session.update(namespace + ".updateSnsOfInformation", vo);
    }

    @Override
    public int updateInfo(Member_informationVO vo) {
        return session.update(namespace + ".updateInfo", vo);
    }

    @Override
    public int updatePicture(Member_informationVO vo) {
        return session.update(namespace + ".updatePicture", vo);
    }

    @Override
    public String findMemberIdByRecommendCode(String recommend_code) {
        return session.selectOne(namespace + ".findMemberIdByRecommendCode", recommend_code);
    }

    @Override
    public int request_point_exchange(PointVO vo) {
        return session.insert(namespace + ".request_point_exchange", vo);
    }

    @Override
    public double totalPoint_of_member(String member_seq) {
        return session.selectOne(namespace + ".totalPoint_of_member", member_seq);
    }

    @Override
    public List<PointVO> list_of_point(Criteria cri) {
        return session.selectList(namespace + ".list_of_point_by_member_seq", cri);
    }

    @Override
    public int totalCountOfMemberPoint(Criteria cri) {
        return session.selectOne(namespace + ".totalCountOfMemberPoint", cri);
    }

    @Override
    public int password_update(MemberVO memberVO) {
        return session.update(namespace + ".password_update", memberVO);
    }


}
