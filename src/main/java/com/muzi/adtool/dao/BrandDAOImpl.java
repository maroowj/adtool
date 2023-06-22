package com.muzi.adtool.dao;

import com.muzi.adtool.domain.BrandVO;
import com.muzi.adtool.domain.CampaignVO;
import com.muzi.adtool.domain.Criteria;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandDAOImpl implements BrandDAO{

    String namespace = "BrandMapper";

    @Autowired
    SqlSession session;

    @Override
    public List<BrandVO> brand_all_list(BrandVO vo) {
        return session.selectList(namespace + ".brand_all_list", vo);
    }

    @Override
    public BrandVO brand_read_by_seq(String brand_seq) {
        return session.selectOne(namespace + ".brand_read_by_seq", brand_seq);
    }

    @Override
    public int brand_insert(BrandVO vo) {
        return session.insert(namespace + ".brand_insert", vo);
    }

    @Override
    public int brand_update(BrandVO vo) {
        return session.update(namespace + ".brand_update", vo);
    }

    @Override
    public int brand_delete(String brand_seq) {
        return session.update(namespace + ".brand_delete", brand_seq);
    }

    @Override
    public int campaignCount_by_brand_seq(String brand_seq) {
        return session.selectOne(namespace + ".campaignCount_by_brand_seq", brand_seq);
    }

    @Override
    public int brand_hidden(BrandVO vo) {
        return session.update(namespace + ".brand_hidden", vo);
    }


}
