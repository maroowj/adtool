package com.muzi.adtool.dao;

import com.muzi.adtool.domain.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO {
    String namespace = "AdminMapper";

    @Autowired
    SqlSession session;

    @Override
    public List<CampaignVO> brand_list_by_country_for_admin(Criteria cri) {
        return session.selectList(namespace + ".brand_list_by_country_for_admin", cri);
    }

    @Override
    public List<CampaignVO> campaign_list_by_brand_seq_and_country(Criteria cri) {
        return session.selectList(namespace + ".campaign_list_by_brand_seq_and_country", cri);
    }

    @Override
    public CampaignVO firstCampaign_by_brand(String brand_seq) {
        return session.selectOne(namespace + ".firstCampaign_by_brand", brand_seq);
    }

    @Override
    public List<PaymentVO> joined_member_list_by_campaign_seq(String campaign_seq) {
        return session.selectList(namespace + ".joined_member_list_by_campaign_seq", campaign_seq);
    }

    @Override
    public int joined_member_info_update(JoinedVO vo) {
        return session.update(namespace + ".joined_member_info_update", vo);
    }

    @Override
    public int joined_member_chat_update(JoinedVO vo) {
        return session.update(namespace + ".joined_member_chat_update", vo);
    }

    @Override
    public int joined_member_accept_update(JoinedVO vo) {
        return session.update(namespace + ".joined_member_accept_update", vo);
    }

    @Override
    public int review_check_update(JoinedVO vo) {
        return session.update(namespace + ".review_check_update", vo);
    }

    @Override
    public List<PaymentVO> payment_list_by_campaign_seq(String campaign_seq) {
        return session.selectList(namespace + ".payment_list_by_campaign_seq", campaign_seq);
    }

    // Payment
    @Override
    public int payment_insert(PaymentVO vo) {
        return session.insert(namespace + ".payment_insert", vo);
    }

    @Override
    public int payment_delete(String joined_campaign_seq) {
        return session.delete(namespace + ".payment_delete", joined_campaign_seq);
    }

    @Override
    public PaymentVO payment_read_by_joined_seq(String joined_campaign_seq) {
        return session.selectOne(namespace + ".payment_read_by_joined_seq", joined_campaign_seq);
    }

    @Override
    public int payment_update(PaymentVO vo) {
        return session.update(namespace + ".payment_update", vo);
    }

    @Override
    public List<Member_informationVO> member_all_list_except_for_admin(Criteria cri) {
        return session.selectList(namespace + ".member_all_list_except_for_admin", cri);
    }

    @Transactional
    @Override
    public int member_info_update(Member_informationVO vo) {
        return session.update(namespace + ".joined_member_info_update", vo);
    }

    @Override
    public int member_all_count_except_for_admin(Criteria cri) {
        return session.selectOne(namespace + ".member_all_count_except_for_admin", cri);
    }

    @Override
    public List<BrandVO> brand_list_by_country_in_reservation_campaign(Criteria cri) {
        return session.selectList(namespace + ".brand_list_by_country_in_reservation_campaign", cri);
    }

    @Override
    public List<Reservation_campaignVO> reservation_campaign_list_by_brand_seq_and_country(Criteria cri) {
        return session.selectList(namespace + ".reservation_campaign_list_by_brand_seq_and_country", cri);
    }

    @Override
    public int reservation_campaign_insert(Reservation_campaignVO vo) {
        return session.insert(namespace + ".reservation_campaign_insert", vo);
    }

    @Override
    public Reservation_campaignVO reservationCampaignRead(String re_campaign_seq) {
        return session.selectOne(namespace + ".reservationCampaignRead", re_campaign_seq);
    }

    @Override
    public void reservationCampaignDeleteByBrandSeq(String brand_seq) {
        session.update(namespace + ".reservationCampaignDeleteByBrandSeq", brand_seq);
    }

    @Override
    public void reservationCampaignDeleteBySeq(String re_campaign_seq) {
        session.update(namespace + ".reservationCampaignDeleteBySeq", re_campaign_seq);
    }

    @Override
    public int update_point_status(String member_seq) {
        return session.update(namespace + ".update_point_status", member_seq);
    }

    @Override
    public int insert_point_by_admin(PointVO vo) {
        return session.insert(namespace + ".insert_point_by_admin", vo);
    }

    @Override
    public BasicVO find_basic_by_seq() {
        return session.selectOne(namespace + ".find_basic_by_seq");
    }

    @Override
    public List<PointVO> all_list_point(Criteria cri) {
        return session.selectList(namespace + ".all_list_point", cri);
    }

    @Override
    public int totalCount_point(Criteria cri) {
        return session.selectOne(namespace + ".totalCount_point", cri);
    }

    @Override
    public int update_point_for_exchange(PointVO vo) {
        return session.update(namespace + ".update_point_for_exchange", vo);
    }

    @Override
    public void member_banned(Member_informationVO vo) {
        session.update(namespace + ".banned_member", vo);
    }

    @Override
    public int password_default(MemberVO memberVO) {
        return session.update(namespace + ".password_default", memberVO);
    }

    @Override
    public int member_delete(String member_seq) {
        return session.update(namespace + ".member_delete", member_seq);
    }

    @Override
    public int update_outDate(String member_seq) {
        return session.update(namespace + ".update_outDate", member_seq);
    }

}
