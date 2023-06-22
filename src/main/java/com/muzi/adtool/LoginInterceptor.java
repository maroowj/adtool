package com.muzi.adtool;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String type= (String) request.getSession().getAttribute("type");
        String member_grade= (String) request.getSession().getAttribute("member_grade");

        String destUri = request.getRequestURI();
        String destQuery = request.getQueryString();
        String dest = (destQuery == null) ? destUri : destUri+"?"+destQuery; // 원래 가려던 경로(uri)


        if(type!=null) { // 로그인 여부 검사. (로그인을 하면 session에 member_type 을 담아 놓음)
            if(dest.contains("/admin")){ // 가려는 uri에 '/admin'이 포함 되어 있는지 확인(관리자 페이지)
                if(type.equals("ROLE_ADMIN")){ // 로그인 한 회원이 관리자일 때,
                    return true;
                }else{ // 로그인 한 회원이 일반 사용자일 때
                    request.getSession().setAttribute("dest", dest); // 인터셉터에 걸리면 우선 session에 dest를 넣어 둠
                    response.sendRedirect("/adm_login");
                    return false;
                }
            }else{
                if(member_grade.equals("2")){
                    return true;
                }else{
//                    System.out.println("no222");
                    if(dest.contains("/campaign")){
//                        System.out.println(dest);
                        response.sendRedirect("/join");
                        return false;
                    }
                    response.sendRedirect("/join");
                    return false;
                }
            }
        }else{
            if(dest.contains("/admin")){ // 가려는 uri에 '/admin'이 포함 되어 있는지 확인(관리자 페이지)
                request.getSession().setAttribute("dest", dest); // 인터셉터에 걸리면 우선 session에 dest를 넣어 둠
                response.sendRedirect("/adm_login");
                return false;
            }
            request.getSession().setAttribute("dest", dest); // 인터셉터에 걸리면 우선 session에 dest를 넣어 둠
            response.sendRedirect("/login");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex) throws Exception {

    }

}
