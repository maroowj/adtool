package com.muzi.adtool.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		// 헤더에서 jwt read
//		System.out.println("request : " + ((HttpServletRequest) request).getHeaderNames());
		String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
//		System.out.println("Token : " + token);
		// 토큰 유효 체크
		if (token != null && jwtTokenProvider.validateToken(token, "Access")) {
			// 토큰 유효시 유저정보 read
			Authentication authentication = jwtTokenProvider.getAuthentication(token,"Access");
			// SecurityContext 에 Authentication 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}
