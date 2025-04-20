package com.highfive.meetu.domain.auth.personal.service;

import com.highfive.meetu.domain.auth.personal.dto.KakaoTokenResponseDTO;
import com.highfive.meetu.domain.auth.personal.dto.KakaoUserResponse;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;


import com.highfive.meetu.infra.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {
    @Value("${api.kakao.client-id}")
    private String clientId;

    @Value("${api.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${api.kakao.token-uri}")
    private String tokenUri;

    @Value("${api.kakao.user-info-uri}")
    private String userInfoUri;

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;

    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<?> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoTokenResponseDTO> response = restTemplate.postForEntity(tokenUri, request, KakaoTokenResponseDTO.class);

        return response.getBody().getAccess_token();
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, KakaoUserResponse.class);

        return response.getBody();
    }

    public LoginResponseDTO kakaoLogin(String code) {
        String kakaoAccessToken = requestAccessToken(code);
        KakaoUserResponse user = getUserInfo(kakaoAccessToken);
        String email = user.getKakao_account().getEmail();
        String nickname = user.getKakao_account().getProfile().getNickname();

        Account account = accountRepository.findByEmailAndAccountType(email, 0)
            .orElseGet(() -> {
                Account newAccount = Account.builder()
                    .email(email)
                    .name(nickname)
                    .accountType(0) // 개인회원
                    .status(0)
                    .build();
                return accountRepository.save(newAccount);
            });

        String accessToken = jwtProvider.generateAccessToken(account.getId());
        String refreshToken = jwtProvider.generateRefreshToken(account.getId());

        return new LoginResponseDTO(accessToken, refreshToken);
    }
}
