package com.muzi.adtool.services;

import com.muzi.adtool.dao.AdminDAO;
import com.muzi.adtool.dao.BrandDAO;
import com.muzi.adtool.dao.CampaignDAO;
import com.muzi.adtool.domain.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BrandServiceImpl implements BrandService{

    @Autowired
    GetSequenceService getSequenceService;

    @Autowired
    BrandDAO brandDAO;

    @Autowired
    CampaignDAO campaignDAO;

    @Autowired
    AdminDAO adminDAO;

    @Override
    @Transactional
    public int insert(BrandVO vo) {
        vo.setBrand_seq(getSequenceService.getSeq("brad"));
        return brandDAO.brand_insert(vo);
    }

    @Override
    public int delete_brand(String brand_seq) {
        campaignDAO.campaignDeleteByBrandSeq(brand_seq);
        adminDAO.reservationCampaignDeleteByBrandSeq(brand_seq);
        return brandDAO.brand_delete(brand_seq);
    }
}
