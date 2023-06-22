package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class JoinedVO extends Member_informationVO{

    private String joined_campaign_seq;
    private String campaign_seq;
    private String member_seq;
    private String accept;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date joined_date;
    private int chat_check;
    private String review_img1;
    private String review_img2;
    private String review_img3;
    private String review_img4;
    private String review_img5;
    private String payment_img1;
    private String payment_img2;
    private String payment_img3;
    private String payment_img4;
    private String payment_img5;
    private int review_check;

    private int rownum;
}
