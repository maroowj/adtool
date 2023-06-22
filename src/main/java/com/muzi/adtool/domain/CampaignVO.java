package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CampaignVO extends BrandVO{
	private String campaign_seq;
	private String brand_seq;
	private String campaign_title;
	private String campaign_country;
	private String type;
	private int need_number;
	private int multiple_number;
	private String campaign_description;
	private String status;
	private String original_price;
	private String discount_price;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date campaign_date;
	private String campaign_img1;
	private String campaign_img2;
	private String campaign_img3;
	private String campaign_img4;
	private String campaign_img5;
	private String campaign_img6;
	private String campaign_img7;
	private String campaign_img8;
	private String campaign_img9;
	private String campaign_img10;

	private int apply_count;
}
