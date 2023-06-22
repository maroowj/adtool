package com.muzi.adtool.controller;

import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.*;
import com.muzi.adtool.security.JwtTokenProvider;
import com.muzi.adtool.services.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping
public class MemberController {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MemberDAO memberDAO;

	@Autowired
	MemberService memberService;

//	@GetMapping("/member")
//	public String memberMainJsp(Model model){
//		model.addAttribute("member", "사용자 전용");
//		return "/user/main";
//	};

	@PostMapping("/common/api/member/insert")
	@ResponseBody
	public int join(MemberVO vo) {
		return memberService.insert(vo);
	}

	@PatchMapping("/common/api/member/update")
	@ResponseBody
	public int update(Member_informationVO vo, String member_id, String step){
		MemberVO memberVO = memberDAO.findByMemberId(member_id);
		if(memberVO != null) {
			vo.setMember_seq(memberVO.getMember_seq());
			if(step.equals("2")) {
				//System.out.println("step2= "+vo.toString());
				memberDAO.updatePreferencesOfInformation(vo);
			}else if(step.equals("3")) {
				//System.out.println("step3= "+vo.toString());
				memberDAO.updateSnsOfInformation(vo);
			}
		}else{
			if(step.equals("all")) {
//				System.out.println("all= "+vo.toString());
				memberDAO.updateInfo(vo);
			}else if(step.equals("picture")) {
//				System.out.println("step= " + vo.toString());

				memberDAO.updatePicture(vo);
			}
		}
		return 1;
	}
	/*********************************** My page ***********************************/
	// 사용자 정보
	@GetMapping("/user/api/member/read")
	@ResponseBody
	public Member_informationVO infoReadByMemberSeq(String member_seq){
		return memberDAO.infoReadByMemberSeq(member_seq);
	}

	// 220608 포인트 환급 요청
	@PostMapping("/user/api/point/request")
	@ResponseBody
	public int request_point_exchange(PointVO vo) {
		return memberService.request_point_exchange(vo);
	}

	// 220608 사용자 포인트 내역
	@GetMapping("/user/api/point")
	@ResponseBody
	public Map<String, Object> MemberPoint(Criteria cri) {
		Map<String, Object> data = new HashMap<>();
		PageMaker pm = new PageMaker();
		pm.setCri(cri);
		pm.setTotalCount(memberDAO.totalCountOfMemberPoint(cri));
		List<PointVO> pointList = memberDAO.list_of_point(cri);
		double totalPoint = 0;
		if(pointList.size()!=0) {
			totalPoint = memberDAO.totalPoint_of_member(cri.getMember_seq());
		}
		data.put("totalPoint", totalPoint);
		data.put("list", pointList);
		data.put("pm", pm);
		data.put("cri", cri);
		return data;
	}
}