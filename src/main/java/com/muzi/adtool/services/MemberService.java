package com.muzi.adtool.services;

import com.muzi.adtool.domain.CertificationVO;
import com.muzi.adtool.domain.MemberVO;
import com.muzi.adtool.domain.PointVO;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface MemberService {
	public int insert(MemberVO vo);

	// 220608 포인트 환급 요청
	public int request_point_exchange(PointVO vo);

	// 메일 인증을 통한 비밀번호 변경
	public int resetPasswordByMail(CertificationVO certificationVO);
}
