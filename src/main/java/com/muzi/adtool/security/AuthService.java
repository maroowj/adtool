package com.muzi.adtool.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

	private final AuthDAO authDAO;
	// UserDetailsService
	@Override
	public AuthVO loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Optional<AuthVO> vo = Optional.ofNullable(authDAO.findByMemberId(memberId));
		return vo.orElseThrow(() -> new UsernameNotFoundException("member not exit"));
		/*
		return dao.readByMemberId(memberId)
				.orElseThrow(() -> new UsernameNotFoundException(memberId + " is not exist."));
	*/
	}

	// memberDTO.setType(Collections.singletonList("ROLE_USER"));
}