package com.highfive.meetu.domain.auth.personal.service;

import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.auth.personal.dto.NaverTokenResponseDTO;
import com.highfive.meetu.domain.auth.personal.dto.NaverUserResponse;
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
public class NaverLoginService {
    @Value("${api.naver.client-id}")
    private String clientId;

    @Value("${api.naver.client-secret}")
    private String clientSecret;

    @Value("${api.naver.redirect-uri}")
    private String redirectUri;

    @Value("${api.naver.token-uri}")
    private String tokenUri;

    @Value("${api.naver.user-info-uri}")
    private String userInfoUri;

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;

    public LoginResponseDTO naverLogin(String code, String state) {
        String accessToken = requestAccessToken(code, state);
        NaverUserResponse user = getUserInfo(accessToken);
        String email = user.getResponse().getEmail();
        String name = user.getResponse().getName();

        Account account = accountRepository.findByEmail(email)
            .orElseGet(() -> {
                Account newAccount = Account.builder()
                    .email(email)
                    .name(name)
                    .accountType(0)
                    .status(0)
                    .build();
                return accountRepository.save(newAccount);
            });

        String jwtAccessToken = jwtProvider.generateAccessToken(account.getId());
        String jwtRefreshToken = jwtProvider.generateRefreshToken(account.getId());

        return new LoginResponseDTO(jwtAccessToken, jwtRefreshToken);
    }

    private String requestAccessToken(String code, String state) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<?> request = new HttpEntity<>(params, headers);
        ResponseEntity<NaverTokenResponseDTO> response = restTemplate.postForEntity(tokenUri, request, NaverTokenResponseDTO.class);

        return response.getBody().getAccess_token();
    }

    private NaverUserResponse getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<NaverUserResponse> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, NaverUserResponse.class);

        return response.getBody();
    }
}
