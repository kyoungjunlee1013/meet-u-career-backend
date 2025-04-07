package com.highfive.meetu.infra.jwt;

import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final ProfileRepository profileRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    // JWT 서명에 사용할 키
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long accountId) {
        return generateToken(accountId, accessTokenExpiration);
    }

    public String generateRefreshToken(Long accountId) {
        return generateToken(accountId, refreshTokenExpiration);
    }

    // 공통 JWT 생성 메서드
    private String generateToken(Long accountId, long expiration) {
        // accountId로 profile을 가져오기
        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다."));

        Long profileId = profile.getId();

        return Jwts.builder()
            .setSubject(accountId.toString())
            .claim("accountId", accountId)
            .claim("profileId", profileId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public Long parseToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject());
    }
}
