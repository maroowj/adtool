package com.muzi.adtool.services;

import com.muzi.adtool.domain.CampaignVO;
import org.springframework.stereotype.Service;

@Service
public interface CampaignService {
	public int insert(CampaignVO vo);
	public int update_image_order(CampaignVO vo);

	public void reservationCampaignPublish();
}
