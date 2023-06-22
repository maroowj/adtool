package com.muzi.adtool.controller.VIEW;

import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.MemberVO;
import com.muzi.adtool.domain.Member_informationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserViewController {

    @Autowired
    MemberDAO memberDAO;

    /** 메인 페이지 **/
    // default
    @GetMapping("/")
    public String main(@CookieValue(name="adtool", required = false) String cookie, HttpSession session) {
        if(cookie != null) {
            MemberVO mvo = memberDAO.findByMemberId(cookie);
            Member_informationVO ivo = memberDAO.infoReadByMemberSeq(mvo.getMember_seq());
            session.setAttribute("member_id", mvo.getMember_id());
            session.setAttribute("member_seq", mvo.getMember_seq());
            session.setAttribute("member_name", ivo.getMember_name());
            session.setAttribute("member_grade", ivo.getMember_grade());
            session.setAttribute("type", mvo.getType());
            if(session.getAttribute("type").equals("ROLE_ADMIN")){
                return "main";
            }else{
                return "main";
            }
        }else {
            return "main";
        }
    }
    // kr
    @GetMapping("/kr")
    public String main_Kr(@CookieValue(name="adtool", required = false) String cookie, HttpSession session) {
        if(cookie != null) {
            MemberVO mvo = memberDAO.findByMemberId(cookie);
            Member_informationVO ivo = memberDAO.infoReadByMemberSeq(mvo.getMember_seq());
            session.setAttribute("member_id", mvo.getMember_id());
            session.setAttribute("member_seq", mvo.getMember_seq());
            session.setAttribute("member_name", ivo.getMember_name());
            session.setAttribute("member_grade", ivo.getMember_grade());
            session.setAttribute("type", mvo.getType());
            if(session.getAttribute("type").equals("ROLE_ADMIN")){
                return "main";
            }else{
                return "main";
            }
        }else {
            return "main";
        }
    }

    /** 회원가입 **/
    // default
    @GetMapping("/join")
    public String joinJsp(Model model, String member_id) {
        model.addAttribute("member_id", member_id);
        return "/user/join";
    }
    // kr
    @GetMapping("/kr/join")
    public String joinJsp_kr(Model model, String member_id) {
        model.addAttribute("member_id", member_id);
        return "/user/join";
    }

    /** 친구 추천 회원가입 **/
    // default
    @GetMapping("/join/{recommend_code}")
    public String joinJsp(Model model, String member_id, @PathVariable(name = "recommend_code") String recommend_code) {
        model.addAttribute("member_id", member_id);
        String recommender = memberDAO.findMemberIdByRecommendCode(recommend_code);
        if(recommender!=null) {
            model.addAttribute("recommender", recommender);
        }else {
            model.addAttribute("noRecommender", true);
        }
        return "/user/join";
    }
    // kr
    @GetMapping("/kr/join/{recommend_code}")
    public String joinJsp_kr(Model model, String member_id, @PathVariable(name = "recommend_code") String recommend_code) {
        model.addAttribute("member_id", member_id);
        String recommender = memberDAO.findMemberIdByRecommendCode(recommend_code);
        if(recommender!=null) {
            model.addAttribute("recommender", recommender);
        }else {
            model.addAttribute("noRecommender", true);
        }
        return "/user/join";
    }


    /** 로그인 페이지 **/
    // default
    @GetMapping("/login")
    public String loginView(){
        return "/user/login";
    }
    // kr
    @GetMapping("/kr/login")
    public String loginView_kr(){
        return "/user/login";
    }


    /** 비밀번호 변경 페이지 **/
    // default
    @GetMapping("/reset/{certification_code}")
    public String resetPasswordView(Model model, @PathVariable(name = "certification_code") String certification_code) {
        model.addAttribute("certificationCode", certification_code);
        return "/user/password_mail";
    }
    // kr
    @GetMapping("/kr/reset/{certification_code}")
    public String resetPasswordView_kr(Model model, @PathVariable(name = "certification_code") String certification_code) {
        model.addAttribute("certificationCode", certification_code);
        return "/user/password_mail";
    }

    /** 캠페인 상세 페이지**/
    // default
    @GetMapping("/campaign/join")
    public String joinView(Model model, String campaign_seq){
        model.addAttribute("campaign_seq", campaign_seq);
        return "/user/campaign/join";
    }
    // kr
    @GetMapping("/kr/campaign/join")
    public String joinView_kr(Model model, String campaign_seq){
        model.addAttribute("campaign_seq", campaign_seq);
        return "/user/campaign/join";
    }

    /** 마이페이지 **/
    // default
    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        if(session.getAttribute("member_seq")==null){
            return "redirect:/login";
        }else{
            model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
            return "/user/mypage/information";
        }
    }
    // kr
    @GetMapping("/kr/mypage")
    public String mypage_kr(Model model, HttpSession session) {
        if(session.getAttribute("member_seq")==null){
            return "redirect:/login";
        }else{
            model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
            return "/user/mypage/information";
        }
    }

    /** 회원정보수정 **/
    // default
    @GetMapping("/mypage/info")
    public String info(Model model, String member_seq, HttpSession session){
        model.addAttribute("seq", member_seq);
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/information";
    }
    // kr
    @GetMapping("/kr/mypage/info")
    public String info_kr(Model model, String member_seq, HttpSession session){
        model.addAttribute("seq", member_seq);
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/information";
    }

    /** 회원비밀번호 변경 **/
    // default
    @GetMapping("/mypage/reset-password")
    public String resetPasswordView() {
        return "/user/mypage/password_mypage";
    }
    // kr
    @GetMapping("/kr/mypage/reset-password")
    public String resetPasswordView_kr() {
        return "/user/mypage/password_mypage";
    }

    /** 마이페이지 포인트 **/
    // default
    @GetMapping("/mypage/point")
    public String pointView(HttpSession session, Model model) {
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/point";
    }
    // kr
    @GetMapping("/kr/mypage/point")
    public String pointView_kr(HttpSession session, Model model) {
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/point";
    }


    /** 참여중인 캠페인 목록 **/
    // default
    @GetMapping("/mypage/joining_campaign")
    public String joining_campaign(Model model, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/joining";
    }
    // kr
    @GetMapping("/kr/mypage/joining_campaign")
    public String joining_campaign_kr(Model model, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/joining";
    }


    /** 종료된 캠페인 목록 **/
    // default
    @GetMapping("/mypage/ended_campaign")
    public String ended_campaign(Model model, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/ended";
    }
    // kr
    @GetMapping("/kr/mypage/ended_campaign")
    public String ended_campaign_kr(Model model, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/ended";
    }



    /** 쪽지 목록 **/
    // default
    @GetMapping("/mypage/message_list")
    public String message_list(Model model, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/msg_list";
    }
    // kr
    @GetMapping("/kr/mypage/message_list")
    public String message_list_kr(Model model, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        return "/user/mypage/msg_list";
    }


    /** 쪽지 읽기 **/
    // default
    @GetMapping("/mypage/message_read")
    public String message_read(Model model, String mno, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        model.addAttribute("mno", mno);
        return "/user/mypage/msg_read";
    }
    // kr
    @GetMapping("/kr/mypage/message_read")
    public String message_read_kr(Model model, String mno, String member_seq, HttpSession session){
        model.addAttribute("vo", memberDAO.infoReadByMemberSeq((String)session.getAttribute("member_seq")));
        model.addAttribute("mno", mno);
        return "/user/mypage/msg_read";
    }

    /** 공지사항 목록 **/
    // default
    @GetMapping("/notice")
    public String user_notice_view(){
        return "/user/notice/list";
    }
    // kr
    @GetMapping("/kr/notice")
    public String user_notice_view_kr(){
        return "/user/notice/list";
    }


    /** 공지사항 읽기 **/
    // default
    @GetMapping("/notice/read")
    public String user_notice_read(){
        return "/user/notice/read";
    }
    // kr
    @GetMapping("/kr/notice/read")
    public String user_notice_read_kr(){
        return "/user/notice/read";
    }


    /** Help 게시판 목록 **/
    // default
    @GetMapping("/help")
    public String user_help_view(){
        return "/user/help/list";
    }
    // kr
    @GetMapping("/kr/help")
    public String user_help_view_kr(){
        return "/user/help/list";
    }

    /** Help 게시판 읽기 **/
    // default
    @GetMapping("/help/read")
    public String user_help_read(){
        return "/user/help/read";
    }
    // kr
    @GetMapping("/kr/help/read")
    public String user_help_read_kr(){
        return "/user/help/read";
    }
}
