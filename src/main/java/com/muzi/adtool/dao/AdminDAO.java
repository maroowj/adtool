package com.muzi.adtool.dao;

import com.muzi.adtool.domain.*;

import java.util.List;

public interface AdminDAO {
    // Brand
    public List<CampaignVO> brand_list_by_country_for_admin(Criteria cri);

    // Campaign
    public List<CampaignVO> campaign_list_by_brand_seq_and_country(Criteria cri);
    public CampaignVO firstCampaign_by_brand(String brand_seq);

    // Joined Campaign
    public List<PaymentVO> joined_member_list_by_campaign_seq(String campaign_seq);
    public int joined_member_info_update(JoinedVO vo);
    public int joined_member_chat_update(JoinedVO vo);
    public int joined_member_accept_update(JoinedVO vo);
    public int review_check_update(JoinedVO vo);

    // Payment
    public List<PaymentVO> payment_list_by_campaign_seq(String campaign_seq);
    public int payment_insert(PaymentVO vo);
    public int payment_delete(String joined_campaign_seq);
    public PaymentVO payment_read_by_joined_seq(String joined_campaign_seq);
    public int payment_update(PaymentVO vo);

    // Member
    public List<Member_informationVO> member_all_list_except_for_admin(Criteria cri);
    public int member_info_update(Member_informationVO vo);
    public int member_all_count_except_for_admin(Criteria cri);


    // Reservation_Campaign
    public List<BrandVO> brand_list_by_country_in_reservation_campaign(Criteria cri);
    public List<Reservation_campaignVO> reservation_campaign_list_by_brand_seq_and_country(Criteria cri);
    public int reservation_campaign_insert(Reservation_campaignVO vo);
    public Reservation_campaignVO reservationCampaignRead(String re_campaign_seq);
    public void reservationCampaignDeleteByBrandSeq(String brand_seq);
    public void reservationCampaignDeleteBySeq(String re_campaign_seq);

    // point 220607
    public int update_point_status(String member_seq);
    public int insert_point_by_admin(PointVO vo);
    public BasicVO find_basic_by_seq();
    public List<PointVO> all_list_point(Criteria cri);
    public int totalCount_point(Criteria cri);
    public int update_point_for_exchange(PointVO vo);

    // banned 220621
    public void member_banned(Member_informationVO vo);

    // 회원 정보 초기화 및 삭제
    public int password_default(MemberVO memberVO);
    public int member_delete(String member_seq);
    public int update_outDate(String member_seq);
}
