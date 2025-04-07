package com.highfive.meetu.infra.jwt;

import com.highfive.meetu.global.util.JwtErrorResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Long userId = jwtProvider.parseToken(token);

                // 인증 객체 생성 및 SecurityContext 등록
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, List.of() // 권한이 없다면 빈 리스트
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                JwtErrorResponseUtil.sendUnauthorized(response, "유효하지 않거나 만료된 토큰입니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
