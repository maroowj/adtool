package com.muzi.adtool.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PaymentVO extends JoinedVO{
    private String payment_seq;
    private String joined_campaign_seq;
    private String payment_platform;
    private int expense;
    private int exchange_rate;
    private int fee;
    private int compensation;
    private int total;
    private String payment_status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date payment_date;
}
