package com.muzi.adtool.dao;


import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.domain.Reservation_campaignVO;

import java.util.Date;
import java.util.List;

public interface CampaignDAO {
    public List<CampaignVO> campaign_all_list_by_country_and_status(Criteria cri);
    public CampaignVO campaign_read_by_seq(String campaign_seq);
    public int campaign_insert(CampaignVO vo);
    public int campaign_update(CampaignVO vo);
    public int campaign_status_update(CampaignVO vo);
    public int campaign_delete(String campaign_seq);
    public int totalCount(Criteria cri);

    public int campaign_update_image_order(CampaignVO vo);

    public int campaignDeleteByBrandSeq(String brand_seq);

    public List<Reservation_campaignVO> reservationCampaignList(String searchDate);
    public void updatePublishStatus(String re_campaign_seq);
}
