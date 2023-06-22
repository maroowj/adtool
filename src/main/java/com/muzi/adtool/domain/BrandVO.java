package com.muzi.adtool.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BrandVO {
    private String brand_seq;
    private String brand_name;
    private String image;
    private String brand_description;
    private int hidden;
}
