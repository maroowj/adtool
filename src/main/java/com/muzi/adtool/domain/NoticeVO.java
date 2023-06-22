package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class NoticeVO extends Member_informationVO{
    private String notice_seq;
    private String title;
    private String content;
    private String writer;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Timestamp notice_date;
    private int read_count;
    private String image1;
    private String  image2;
    private String image3;
    private String image4;
    private String image5;
    private int highlight;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Timestamp update_date;

    private int rowNum;
}
