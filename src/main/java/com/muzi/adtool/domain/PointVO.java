package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import javassist.compiler.ast.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PointVO extends Member_informationVO {
    private String point_seq;
    private String member_seq;
    private String campaign_seq;
    private String campaign_title;
    private double point;
    private String type;
    private String detail;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date request_date;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date response_date;
    private int status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date create_date;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date update_date;
    private String recommender_id;
    private String recommender_seq;
}
