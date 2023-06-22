package com.muzi.adtool.controller.API;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainAPI {

	@PostMapping("/friends")
	public Map<String, Object> friends() {
		Map<String, Object> result = new HashMap<>();
		return result;
	}
}