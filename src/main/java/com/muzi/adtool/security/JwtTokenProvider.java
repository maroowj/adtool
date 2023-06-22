package com.muzi.adtool.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { //토큰 생성 클래스

	@Value("${ACCESS_TOKEN_KEY}")
	private String ACCESS_TOKEN_KEY;
	@Value("${REFRESH_TOKEN_KEY}")
	private String REFRESH_TOKEN_KEY;

	private long ACCESS_EXPIRE = 1000 * 60 * 10; // 10분
	private long REFRESH_EXPIRE = 1000 * 60 * 60 * 6; // 6시간

//	private long ACCESS_EXPIRE = 1000 * 10; // 10초 (Test)
//	private long REFRESH_EXPIRE = 1000 * 20; // 20초 (Test)

	private final UserDetailsService userDetailsService;

	// 객체 초기화, secretKey 인코딩
	@PostConstruct
	protected void init() {
		ACCESS_TOKEN_KEY = Base64.getEncoder().encodeToString(ACCESS_TOKEN_KEY.getBytes());
		REFRESH_TOKEN_KEY = Base64.getEncoder().encodeToString(REFRESH_TOKEN_KEY.getBytes());
	}

	// JWT 토큰 생성
	public String createToken(String memberId, List<String> roles, String type) {
		Claims claims = Jwts.claims().setSubject(memberId);
		claims.put("roles", roles);
		Date now = new Date();
		String token = "";
		if (type.equals("Access")) {
			token = Jwts.builder()
					.setClaims(claims)
					.setIssuedAt(now)
					.setExpiration(new Date(now.getTime() + ACCESS_EXPIRE))
					.signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_KEY)
					.compact();
		} else {
			token = Jwts.builder()
					.setClaims(claims)
					.setIssuedAt(now)
					.setExpiration(new Date(now.getTime() + REFRESH_EXPIRE))
					.signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_KEY)
					.compact();
		}
		return token;
	}

	// JWT 토큰 조회
	public Authentication getAuthentication(String token, String type) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getMemberId(token,type));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// JWT 토큰 정보 추출
	public String getMemberId(String token, String type) {
		String MemberId = "";
		if (type.equals("Access")) {
			MemberId = Jwts.parser().setSigningKey(ACCESS_TOKEN_KEY).parseClaimsJws(token).getBody().getSubject();
		} else {
			MemberId = Jwts.parser().setSigningKey(REFRESH_TOKEN_KEY).parseClaimsJws(token).getBody().getSubject();
		}
		return MemberId;
	}

	// header에서 token read
	public String resolveAccessToken(HttpServletRequest request) {
//		System.out.println("RequestHeaderToken : " + request.getHeader("ACCESS_TOKEN"));
		return request.getHeader("ACCESS_TOKEN");
	}

	// 토큰 유효기간 확인
	public boolean validateToken(String token, String type) {
		try {
			Jws<Claims> claims = null;
			if (type.equals("Access")) {
				claims = Jwts.parser().setSigningKey(ACCESS_TOKEN_KEY).parseClaimsJws(token);
			} else {
				claims = Jwts.parser().setSigningKey(REFRESH_TOKEN_KEY).parseClaimsJws(token);
			}
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
