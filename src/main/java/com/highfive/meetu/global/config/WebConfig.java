package com.highfive.meetu.global.config;

import com.highfive.meetu.global.security.LoginAccountIdArgumentResolver;
import com.highfive.meetu.global.security.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvcConfigurer 설정
 * - CORS 설정
 * - 커스텀 ArgumentResolver 등록
 * - 정적 리소스 핸들러 등록 (/static/chat/** → uploads/chat/)
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginAccountIdArgumentResolver loginAccountIdArgumentResolver;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * 커스텀 ArgumentResolver 등록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAccountIdArgumentResolver);
        resolvers.add(loginUserArgumentResolver);
    }

    /**
     * CORS 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://meet-u-career.com",
                        "https://api.meet-u-career.com",
                        "http://localhost:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 정적 리소스 핸들러 등록
     * - 클라이언트에서 /static/chat/** 요청 시 uploads/chat/ 디렉토리에서 파일 제공
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/chat/**")
                .addResourceLocations("file:uploads/chat/");
    }
}
