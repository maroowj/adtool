package com.muzi.adtool;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/mypage/**", "/admin/**", "/user/api/**")
                .excludePathPatterns("/"); // 해당 경로는 인터셉터가 가로채지 않는다.
    }
}
