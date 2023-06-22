package com.muzi.adtool.controller;

import com.muzi.adtool.dao.BrandDAO;
import com.muzi.adtool.domain.BrandVO;
import com.muzi.adtool.domain.Criteria;
import com.muzi.adtool.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BrandController {

    @Autowired
    BrandDAO brandDAO;

    @Autowired
    BrandService brandService;

    @GetMapping("/admin/api/brand/list")
    @ResponseBody
    public List<BrandVO> brandList(BrandVO vo){
        return brandDAO.brand_all_list(vo);
    }

    @PostMapping("/admin/api/brand/insert")
    @ResponseBody
    public int brandInsert(BrandVO vo){
        return brandService.insert(vo);
    }

    @DeleteMapping("/admin/api/brand/delete")
    @ResponseBody
    public int brandDelete(String brand_seq) {
        return brandService.delete_brand(brand_seq);
    }

    @PostMapping("/admin/api/brand/hidden")
    @ResponseBody
    public void brandHidden(String brand_seq) {
        BrandVO vo = brandDAO.brand_read_by_seq(brand_seq);
        if(vo.getHidden()==0) {
            vo.setHidden(1);
        }else {
            vo.setHidden(0);
        }
        brandDAO.brand_hidden(vo);
    }
}
