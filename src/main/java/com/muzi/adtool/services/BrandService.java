package com.muzi.adtool.services;

import com.muzi.adtool.domain.BrandVO;
import org.springframework.stereotype.Service;

@Service
public interface BrandService {
    public int insert(BrandVO vo);

    public int delete_brand(String brand_seq);
}
