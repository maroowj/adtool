package com.muzi.adtool.services;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
//@Controller
@RequiredArgsConstructor
public class Scheduler {

	private final CampaignService campaignService;

	@Scheduled(cron = "0 0/5 * * * *") // 초 분 시
	@SchedulerLock(name = "reservation", lockAtLeastFor = "4m", lockAtMostFor = "4m")
	public void refreshFeed() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String searchDate = sdf.format(new Date());
//		System.out.println("schedule Test >>>>");
		campaignService.reservationCampaignPublish();
	}
}
