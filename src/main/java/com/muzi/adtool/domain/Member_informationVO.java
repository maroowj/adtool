package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Member_informationVO extends MemberVO{

    private String member_info_seq;
    private String member_seq;
    private String member_name;
    private String member_nickname;
    private String country;
    private String country2;
    private String country3;
    private String address1;
    private String address2;
    private String state;
    private String city;
    private String zipcode;
    private String national_number;
    private String phone;
    private String member_picture;
    private String member_grade;
    private String youtube_url;
    private String youtube_subscribes;
    private String instagram_url;
    private String instagram_followers;
    private String facebook_url;
    private String facebook_followers;
    private String tiktok_url;
    private String tiktok_followers;
    private String preferences;
    private String shopee_id;
    private String lazada_id;
    private String paypal_id;
    private String payoneer_id;
    private String recommender;
    private int recommendation;
    private int point_status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date join_date;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date out_date;
    private int banned;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date banned_date;
    private int rowNum;
}
