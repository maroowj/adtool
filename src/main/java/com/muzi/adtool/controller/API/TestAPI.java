package com.muzi.adtool.controller.API;

import com.muzi.adtool.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestAPI {
	private final TestService testService;
	/*
	@PostMapping("/friends")
	public Map<String, Object> friends(@RequestBody List<ListVO> list) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int save = 0, update = 0, delete = 0;
		for (ListVO friend : list) {
			Map<String, Object> type = testService.updateList(friend);
			switch ((String) type.get("type")) {
				case "save" :
					save++;
					break;
				case "update" :
					update++;
					break;
				case "delete" :
					delete++;
					break;
			}
		}
		result.put("save", save);
		result.put("update", update);
		result.put("delete", delete);
		return result;
	}
	*/
}