package com.muzi.adtool.dao;



import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.MemberVO;
import com.muzi.adtool.domain.Member_informationVO;
import com.muzi.adtool.domain.PointVO;

import java.util.List;

public interface MemberDAO {

    public int insert(MemberVO vo);
    public int insertType(MemberVO vo);
    public int insertInfo(Member_informationVO vo);
    public MemberVO findByMemberId(String memberId);
    public List<Member_informationVO> infoList();
    public Member_informationVO infoReadByMemberSeq(String member_seq);
    public int updatePreferencesOfInformation(Member_informationVO vo);
    public int updateSnsOfInformation(Member_informationVO vo);
    public int updateInfo(Member_informationVO vo);
    public int updatePicture(Member_informationVO vo);

    /** 20220607 추가**/
    public String findMemberIdByRecommendCode(String recommend_code);
    public int request_point_exchange(PointVO vo);
    public double totalPoint_of_member(String member_seq);
    public List<PointVO> list_of_point(Criteria cri);
    public int totalCountOfMemberPoint(Criteria cri);

    public int password_update(MemberVO memberVO);
}
