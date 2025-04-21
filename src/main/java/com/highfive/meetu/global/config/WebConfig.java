package com.highfive.meetu.global.config;

import com.highfive.meetu.global.security.LoginAccountIdArgumentResolver;
import com.highfive.meetu.global.security.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvcConfigurer 설정
 * - CORS 설정 및 커스텀 ArgumentResolver 등록
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
     * CORS 설정: 프론트 개발 서버(Next.js) 허용
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}