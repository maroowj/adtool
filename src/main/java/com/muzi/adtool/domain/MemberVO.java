package com.muzi.adtool.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberVO {
    private String member_seq;
    private String member_id;
    private String member_password;
    private String access_token;
    private String refresh_token;
    private int withdrawal;
    private String type;
    private String member_name;
    private String recommend_code;
    private String recommender;
    private String country;
}
