package com.muzi.adtool.security;

public interface AuthDAO {
	public AuthVO findByMemberId(String memberId);
}