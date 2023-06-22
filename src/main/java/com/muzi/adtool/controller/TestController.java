package com.muzi.adtool.controller;

import com.muzi.adtool.services.GetSequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

	@Autowired
	GetSequenceService getSequenceService;


	@GetMapping("")
	public String main() {
		return "test/index";
	}

	@ResponseBody
	@GetMapping("/seq")
	public String getSeq() {
		return getSequenceService.getSeq("test");
	}
}