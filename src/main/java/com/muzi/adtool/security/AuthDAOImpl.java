package com.muzi.adtool.security;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthDAOImpl implements AuthDAO {

	String namespace = "";
	private final SqlSession session;

	@Override
	public AuthVO findByMemberId(String memberId) {
		// insert the method to read member by member_id
		return session.selectOne(namespace + ".", memberId);
	}
}
