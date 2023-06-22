package com.muzi.adtool.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListVO {
	private int sno;
	private int sid;
	private String uuid;
	private String did;
	private String profileImageUrl;
	private boolean checked;
	private boolean removed;
}
