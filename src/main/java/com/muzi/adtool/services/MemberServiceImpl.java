package com.muzi.adtool.services;

import com.muzi.adtool.dao.AdminDAO;
import com.muzi.adtool.dao.CertificationDAO;
import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Random;


@Repository
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	@Autowired
	GetSequenceService getSequenceService;
	
	@Autowired
	MemberDAO memberDAO;

	@Autowired
	AdminDAO adminDAO;

	@Autowired
	CertificationDAO certificationDAO;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Transactional
	@Override
	public int insert(MemberVO vo) {
		int result =0;
		MemberVO memberVO = memberDAO.findByMemberId(vo.getMember_id());
		if(memberVO != null) { /** 이미 가입한 계정이 있음 **/
			result = 0;
		}else { /** 가입한 계정 없음 **/
			vo.setMember_seq(getSequenceService.getSeq("mber"));
			vo.setMember_password(passwordEncoder.encode(vo.getMember_password()));
			vo.setType("ROLE_USER");
			String recommend_code = "";
			recommend_code = generatedRandomKey();

			String findRecommendCode = memberDAO.findMemberIdByRecommendCode(recommend_code);
			if(findRecommendCode!=null) {
				while (findRecommendCode==null){
					recommend_code = generatedRandomKey();
					findRecommendCode = memberDAO.findMemberIdByRecommendCode(recommend_code);
				}
			}
			vo.setRecommend_code(recommend_code);

			Member_informationVO ivo = new Member_informationVO();
			ivo.setMember_info_seq(getSequenceService.getSeq("memi"));
			ivo.setMember_seq(vo.getMember_seq());
			ivo.setMember_name(vo.getMember_name());
			ivo.setMember_grade("0");
			if(vo.getRecommender()!=null && !vo.getRecommender().equals("")) {
				ivo.setRecommender(vo.getRecommender());
				ivo.setRecommendation(1);
			}else {
				ivo.setRecommender(null);
				ivo.setRecommendation(0);
			}
			ivo.setCountry(vo.getCountry());
			memberDAO.insert(vo);
			memberDAO.insertType(vo);
			memberDAO.insertInfo(ivo);
			result=1;
		}
		return result;
	}

	/** 포인트 환급 요청 **/
	@Transactional
	@Override
	public int request_point_exchange(PointVO vo) {
		int result = 0;
		double totalPoint = memberDAO.totalPoint_of_member(vo.getMember_seq());
		BasicVO basicVO = adminDAO.find_basic_by_seq();
		if(vo.getPoint() < basicVO.getMinimum()) {
			result = 0; // 요청한 환급 포인트가 최소 출금액 보다 적을 때, (실패)
		}else if(vo.getPoint() > totalPoint) {
			result = 1; // 요청한 환급 포인트가 회원이 보유한 총 포인트보다 많을 때, (실패)
		}else {
			vo.setPoint_seq(getSequenceService.getSeq("poin"));
			vo.setType("요청");
			vo.setDetail("사용자 출금 요청");
			vo.setPoint(vo.getPoint() * -1);
			memberDAO.request_point_exchange(vo);
			result = 2; // (성공)
		}
		return result;
	}

	@Override
	@Transactional
	public int resetPasswordByMail(CertificationVO certificationVO) {
		int result = 0;
		MemberVO memberVO = memberDAO.findByMemberId(certificationVO.getMember_id());
		if(memberVO.getWithdrawal()==1) {
			result = 2;
		}else {
			memberVO.setMember_password(passwordEncoder.encode(certificationVO.getNpw()));
			memberDAO.password_update(memberVO);
			certificationDAO.updateCertification(certificationVO.getCertification_seq());
			result = 1;
		}


		return result;
	}

	private static String generatedRandomKey() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();
		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		return generatedString;
	}

}
