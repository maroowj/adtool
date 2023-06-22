package com.muzi.adtool.controller;

import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.MemberVO;
import com.muzi.adtool.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDAO memberDAO;

    @PostMapping("/check/access")
    public Map<String, Object> checkAccess(HttpSession session, Model model, String token, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();

        boolean cookie_check = false;

        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication().getPrincipal().equals("anonymousUser")) {
            Cookie cookie = new Cookie("REFRESH_TOKEN", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            result.put("status", 4);
            return result;
        }

        if (jwtTokenProvider.validateToken(token, "Access")) { // ACCESS_TOKEN 유효
            Authentication auth = jwtTokenProvider.getAuthentication(token,"Access");
            securityContext.setAuthentication(auth);
            result.put("status", 0);
        } else { // ACCESS_TOKEN 만료
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("REFRESH_TOKEN")) {
                    cookie_check = true;

                    if (jwtTokenProvider.validateToken(cookie.getValue(), "Refresh")) { // REFRESH_TOKEN 유효
                        String memberId = jwtTokenProvider.getMemberId((String) cookie.getValue(), "Refresh");
                        Optional<MemberVO> member = Optional.ofNullable(memberDAO.findByMemberId(memberId));
                        token = jwtTokenProvider.createToken(member.get().getMember_id(), Collections.singletonList(member.get().getType()), "Access"); // ???

                        result.put("status", 1);
                        result.put("token", token);
                        Authentication auth = jwtTokenProvider.getAuthentication(token, "Access");
                        securityContext.setAuthentication(auth);
                    } else { // REFRESH_TOKEN 만료
                        result.put("status", 2);
                    }
                }
            }

            if(!cookie_check) {
                result.put("status", 3);
            }
        }

        return result;
    }
}
