package com.muzi.adtool.controller;

import com.muzi.adtool.dao.CertificationDAO;
import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.CertificationVO;
import com.muzi.adtool.domain.MemberVO;
import com.muzi.adtool.domain.Member_informationVO;
import com.muzi.adtool.services.MailService;
import com.muzi.adtool.services.MemberService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

	@Resource(name="uploadPath")
	String path;

	@Autowired
	MemberDAO memberDAO;

	@Autowired
	MemberService memberService;

	@Autowired
	MailService mailService;

	@Autowired
	CertificationDAO certificationDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

	/********************************************* 로그인 *********************************************/

	@PostMapping("/common/api/login")
	@ResponseBody
	public HashMap<String, Object> loginPost(String member_id, String member_password, HttpSession session,
											 HttpServletResponse response, HttpServletRequest request, boolean chkLogin) throws Exception {

		HashMap<String, Object> map = new HashMap<>();

		int result = 0;// 계정 없음
		MemberVO mvo = memberDAO.findByMemberId(member_id);

		if(mvo != null) {
			Member_informationVO ivo = memberDAO.infoReadByMemberSeq(mvo.getMember_seq());// 20211019 우람 수정
			if (mvo.getWithdrawal() == 1) {
				result = 3;// 탈퇴한 계정
			} else {
				result = 1;// 계정있음

				if (passwordEncoder.matches(member_password, mvo.getMember_password())) {
					result = 2;// id, password 일치 (로그인 성공)

					session.setAttribute("member_id", mvo.getMember_id());
					session.setAttribute("member_seq", mvo.getMember_seq());
					session.setAttribute("member_name", ivo.getMember_name());
					session.setAttribute("member_grade", ivo.getMember_grade());
					session.setAttribute("type", mvo.getType());
					map.put("type", mvo.getType());

					if(chkLogin==true) { // 로그인 저장 여부
						Cookie cookie = new Cookie("adtool", mvo.getMember_id());// 2021-11-10 우람 수정
						cookie.setPath("/");
						cookie.setMaxAge(60 * 60 * 24 * 7);
						response.addCookie(cookie);
					}
				}
			}
			map.put("member_grade", ivo.getMember_grade());
		}
		String dest = (String)session.getAttribute("dest");
		map.put("result", result);
		map.put("dest", dest);
		return map;
	}

	@GetMapping("/common/api/logout")
	public String logout(HttpServletRequest req, HttpServletResponse res, HttpSession session){
		expiredCookie(res, "adtool");// 2021-11-10 우람 수정
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(session.getAttribute("member_id")!=null) {
			if(session.getAttribute("type").equals("ROLE_ADMIN")) {
				if(authentication != null){
					new SecurityContextLogoutHandler().logout(req,res,authentication);
				}
				return "redirect:/";
			}else {
				if(authentication != null){
					new SecurityContextLogoutHandler().logout(req,res,authentication);
				}
			}
		}
		return "redirect:/";
	}

	private void expiredCookie(HttpServletResponse res, String cookieId) {
		Cookie cookie = new Cookie(cookieId, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		res.addCookie(cookie);
	}

	@PostMapping("/common/api/member/read/duplicate_check")
	@ResponseBody
	public HashMap<String, Object> duplicate_check(String member_id){
		HashMap<String, Object> map = new HashMap<>();

		int result = 0;// 계정 없음
		MemberVO mvo = memberDAO.findByMemberId(member_id);
		if(mvo != null){
			if (mvo.getWithdrawal() == 1) {
				result = 1;// 탈퇴한 계정
			}
			result = 2; // 계정 있음 (중복)
		}
		map.put("result", result);
		return map;
	}

	// 비밀번호 메일 보내기
	@PostMapping("/common/api/member/reset")
	@ResponseBody
	public int reset_password(String member_id) throws MessagingException {
		return mailService.sendMail(member_id);
	}

	@GetMapping("/common/api/certification")
	@ResponseBody
	public Map<String, Object> certification(String certification_code) {
		Map<String, Object> data = new HashMap<>();
		int result=0;

		CertificationVO certificationVO = certificationDAO.findByCode(certification_code);

		if(certificationVO==null) {
			result = 0;
		}else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(certificationVO.getSend_date());
			cal.add(Calendar.DATE, 7);
			Date expiredDate = new Date(cal.getTimeInMillis());
			Date nowDate = new Date();
			int dateCompare = nowDate.compareTo(expiredDate);

			if(certificationVO.getCertificate_date()!=null) {
				result = 0;
			}else if(dateCompare>0) {
				result = 0;
			}else {
				result = 1;
				data.put("certificationInfo", certificationVO);
			}
		}
		data.put("result", result);
		return data;
	}

	@PostMapping("/common/api/member/reset/password")
	@ResponseBody
	public int resetPassword(CertificationVO certificationVO) {
		return memberService.resetPasswordByMail(certificationVO);
	}

	@PostMapping("/user/api/password/confirm")
	@ResponseBody
	public int confirmPassword(String member_id, String member_password, HttpSession session) {
		int result = 0;
		MemberVO mvo = memberDAO.findByMemberId(member_id);

		if(mvo != null) {
			Member_informationVO ivo = memberDAO.infoReadByMemberSeq(mvo.getMember_seq());// 20211019 우람 수정
			if (mvo.getWithdrawal() == 1) {
				result = 3;// 탈퇴한 계정
			} else {
				result = 1;// 계정있음

				if (passwordEncoder.matches(member_password, mvo.getMember_password())) {
					result = 2;// id, password 일치 (로그인 성공)
					session.setAttribute("confirmPwd", true);
					}
				}
			}
		return result;
	}

	@PostMapping("/user/api/password/reset-confirm")
	@ResponseBody
	public void resetConfirm(HttpSession session) {
		session.setAttribute("confirmPwd", false);
	}

	@PostMapping("/user/api/password/reset")
	@ResponseBody
	public int resetPasswordMyPage(MemberVO memberVO) {
		memberVO.setMember_password(passwordEncoder.encode(memberVO.getMember_password()));
		return memberDAO.password_update(memberVO);
	}


	/****************************************** 파일 업로드 ******************************************/

	//이미지 출력
	@ResponseBody //실제 이미지가 리턴될거라서
	@RequestMapping(value = "/displayFile", produces = {"image/png", "image/jpg", "image/bmp", "image/jpeg", "image/gif", "image/tif", "image/tiff"})
	public byte[] display(String fullName)throws Exception {
		FileInputStream in = new FileInputStream(path + "/" + fullName);//읽어들임
		byte[] image = IOUtils.toByteArray(in);
		in.close();
		return image;
	}

//	//이미지 출력
//	@ResponseBody //실제 이미지가 리턴될거라서
//	@RequestMapping("/**")
//	public byte[] displayNew(HttpServletRequest request)throws Exception {
//		FileInputStream in = new FileInputStream(path + "/" + request.getRequestURI());//읽어들임
//		byte[] image = IOUtils.toByteArray(in);
//		in.close();
//		return image;
//	}

	//이미지 업로드
	@RequestMapping(value = "/uploadFile", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> uploadAjaxPost(MultipartHttpServletRequest request, String dir, String limit) throws Exception {
		Map<String,Object> map = new HashMap<>();

		String img = "img";
		int cnt = 1;
		int limitCnt=5;
		File target;
//		System.out.println(">>>"+limit);
		if(limit!=null){
			limitCnt=Integer.parseInt(limit);
		}

		for(int i=0; i<=limitCnt; i++) {
			MultipartFile file = request.getFile("image"+i);

			if (file != null) {
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				String savedName = System.currentTimeMillis() + "." + ext;
				File folder = new File(path + dir);
				if (!folder.exists()) folder.mkdirs();
				target = new File(path + dir, savedName);
				map.put(img + cnt, savedName);
				FileCopyUtils.copy(file.getBytes(), target);
				Thread.sleep(100);
				cnt++;
			}

		}
		return map;
	}

	//이미지 삭제
	@ResponseBody
	@RequestMapping("/deleteFile")
	public void deleteFile(String fullName){
		new File(path+fullName).delete();
	}
}