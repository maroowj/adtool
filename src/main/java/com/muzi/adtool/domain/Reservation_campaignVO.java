package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
public class Reservation_campaignVO extends BrandVO{
	private String re_campaign_seq;
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
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Timestamp reservation_date;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Timestamp publish_date;
	private int	publish_status;
}
