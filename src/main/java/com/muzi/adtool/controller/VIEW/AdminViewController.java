package com.muzi.adtool.controller.VIEW;

import com.muzi.adtool.dao.AdminDAO;
import com.muzi.adtool.dao.CampaignDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminViewController {

    @Autowired
    CampaignDAO cdao;

    @Autowired
    AdminDAO adao;

    /** 관리자 메인 페이지 **/
    // default
    @GetMapping("/admin")
    public String adm_index(){
        return "admin/joining_campaign";
    }

    /** 관리자 로그인 페이지 **/
    // default
    @GetMapping("/adm_login")
    public String adm_login(){
        return "admin/adm_login";
    }

    /** 관리자 현재캠페인 (모집중 + 진행중) **/
    // default
    @GetMapping("/admin/joining_campaign")
    public String joining_campaign_view(Model model, String searchType, String keyword){
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "/admin/joining_campaign";
    }

    /** 관리자 지난캠페인 (종료) **/
    // default
    @GetMapping("/admin/ended_campaign")
    public String ended_campaign_view(Model model, String searchType, String keyword) {
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "/admin/ended_campaign";
    }

    /** 관리자 예약캠페인 **/
    // default
    @GetMapping("/admin/reservation_campaign")
    public String reservation_campaign_view(){
        return "/admin/reservation_campaign";
    }

    /** 관리자 예약캠페인 상세(수정) **/
    // default
    @GetMapping("/admin/reservation_campaign/{re_campaign_seq}")
    public String reservation_read_view(Model model, @PathVariable(name = "re_campaign_seq") String re_campaign_seq) {
        model.addAttribute("re_campaign_seq", re_campaign_seq);
        model.addAttribute("vo", adao.reservationCampaignRead(re_campaign_seq));
        return "/admin/reservation_campaign_update_multiple_ver";
    }

    /** 관리자 캠페인 생성 **/
    // default
    @GetMapping("/admin/campaign_insert")
    public String campaign_insertJsp(){
        return "/admin/campaign_insert_multiple_ver";
    }

    /** 캠페인 수정 **/
    // default
    @GetMapping("/admin/campaign_update")
    public String campaign_updateJsp(Model model, String campaign_seq)
    {
        model.addAttribute("campaign_seq", campaign_seq);
        model.addAttribute("vo", cdao.campaign_read_by_seq(campaign_seq));
        return "/admin/campaign_update_multiple_ver";
    }

    /** 캠페인 신청 명단 **/
    // default
    @GetMapping("/admin/member_in_campaign")
    public String memberList_in_campaign(Model model, String campaign_seq){
        model.addAttribute("campaign_seq", campaign_seq);
        model.addAttribute("cvo", cdao.campaign_read_by_seq(campaign_seq));
        return "/admin/joined_member_list";
    }

    /** 캠페인 리뷰 **/
    // default
    @GetMapping("/admin/campaign_review")
    public String admin_campaign_review(Model model, String campaign_seq){
        model.addAttribute("campaign_seq", campaign_seq);
        model.addAttribute("cvo", cdao.campaign_read_by_seq(campaign_seq));
        return "/admin/joined_campaign_review";
    }

    /** 캠페인 정산 **/
    // default
    @GetMapping("/admin/campaign_calculate")
    public String admin_campaign_calculate(Model model, String campaign_seq){
        model.addAttribute("campaign_seq", campaign_seq);
        model.addAttribute("cvo", cdao.campaign_read_by_seq(campaign_seq));
        return "/admin/joined_campaign_calculate";
    }

    /** 회원관리 **/
    // default
    @GetMapping("/admin/manage_member")
    public String manage_member_view(){
        return "/admin/manage_member";
    }

    /** 포인트 **/
    // default
    @GetMapping("/admin/point")
    public String point_view() {
        return "/admin/point_list";
    }

    /** 쪽지함  목록 **/
    // default
    @GetMapping("/admin/message_box")
    public String message_box_view(){
        return "/admin/message/main";
    }

    /** 쪽지함 읽기 **/
    // default
    @GetMapping("/admin/message_box/read")
    public String message_read_view(){
        return "/admin/message/read";
    }

    /** 공지사항 등록 **/
    // default
    @GetMapping("/admin/notice/insert")
    public String notice_insert_view(){
        return "/admin/notice/insert";
    }

    /** 공지사항 목록 **/
    // default
    @GetMapping("/admin/notice")
    public String admin_notice_view(){
        return "/admin/notice/list";
    }

    /** 공지사항 읽기 **/
    // default
    @GetMapping("/admin/notice/read")
    public String notice_read(){
        return "/admin/notice/read";
    }

    /** 공지사항 수정 **/
    // default
    @GetMapping("/admin/notice/update")
    public String notice_update(){
        return "/admin/notice/update";
    }

    /** Help 게시글 등록 **/
    // default
    @GetMapping("/admin/help/insert")
    public String help_insert_view(){
        return "/admin/help/insert";
    }

    /** Help 게시글 목록 **/
    // default
    @GetMapping("/admin/help")
    public String admin_help_view(){
        return "/admin/help/list";
    }

    /** Help 게시글 읽기 **/
    // default
    @GetMapping("/admin/help/read")
    public String help_read(){
        return "/admin/help/read";
    }

    /** Help 게시글 수정 **/
    // default
    @GetMapping("/admin/help/update")
    public String help_update(){
        return "/admin/help/update";
    }
}
