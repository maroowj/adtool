package com.muzi.adtool.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
//			System.out.println("jwt : " + jwt);
			if (StringUtils.isNotEmpty(jwt) && jwtTokenProvider.validateToken(jwt, "Access")) {
				// 토큰 유효시 유저정보 read
				Authentication authentication = jwtTokenProvider.getAuthentication(jwt,"Access");
				// SecurityContext 에 Authentication 저장
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				if (StringUtils.isEmpty(jwt)) {
					request.setAttribute("unauthorization", "401 인증키 없음.");
				}

				if (jwtTokenProvider.validateToken(jwt, "Access")) {
					request.setAttribute("unauthorization", "401-001 인증키 만료");
				}
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("ACCESS_TOKEN");
		if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		return null;
	}
}
