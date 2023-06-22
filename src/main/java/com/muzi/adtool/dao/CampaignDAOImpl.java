package com.muzi.adtool.dao;

import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.Reservation_campaignVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CampaignDAOImpl implements CampaignDAO{

    String namespace = "CampaignMapper";

    @Autowired
    SqlSession session;


    @Override
    public List<CampaignVO> campaign_all_list_by_country_and_status(Criteria cri) {
        return session.selectList(namespace + ".campaign_all_list_by_country_and_status", cri);
    }

    @Override
    public CampaignVO campaign_read_by_seq(String campaign_seq){
        return session.selectOne(namespace + ".campaign_read_by_seq", campaign_seq);
    }

    @Override
    public int campaign_insert(CampaignVO vo) {
        return session.insert(namespace + ".campaign_insert", vo);
    }

    @Override
    public int campaign_update(CampaignVO vo) {
        return session.update(namespace + ".campaign_update", vo);
    }

    @Override
    public int campaign_status_update(CampaignVO vo) {
        return session.update(namespace + ".campaign_status_update", vo);
    }

    @Override
    public int campaign_delete(String campaign_seq) {
        return session.update(namespace + ".campaign_delete", campaign_seq);
    }

    @Override
    public int totalCount(Criteria cri) {
        return session.selectOne(namespace + ".totalCount", cri);
    }


    @Override
    public int campaign_update_image_order(CampaignVO vo) {
        return session.update(namespace + ".campaign_update_image_order", vo);
    }

    @Override
    public int campaignDeleteByBrandSeq(String brand_seq) {
        return session.update(namespace + ".campaignDeleteByBrandSeq", brand_seq);
    }

    @Override
    public List<Reservation_campaignVO> reservationCampaignList(String searchDate) {
        return session.selectList(namespace + ".reservationCampaignList", searchDate);
    }

    @Override
    public void updatePublishStatus(String re_campaign_seq) {
        session.selectList(namespace + ".updatePublishStatus", re_campaign_seq);
    }
}
