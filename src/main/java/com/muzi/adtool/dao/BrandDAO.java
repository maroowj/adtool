package com.muzi.adtool.dao;

import com.muzi.adtool.domain.BrandVO;
import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.Criteria;

import java.util.List;

public interface BrandDAO {
    public List<BrandVO> brand_all_list(BrandVO vo);
    public BrandVO brand_read_by_seq(String brand_seq);
    public int brand_insert(BrandVO vo);
    public int brand_update(BrandVO vo);
    public int brand_delete(String brand_seq);
    public int campaignCount_by_brand_seq(String brand_seq);

    public int brand_hidden(BrandVO vo);

}
