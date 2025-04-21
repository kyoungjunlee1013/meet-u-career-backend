package com.highfive.meetu.domain.auth.personal.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.highfive.meetu.domain.auth.personal.dto.LoginResponseDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.infra.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    @Value("${api.google.client-id}")
    private String clientId;

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;

    public LoginResponseDTO login(String idToken) {
        GoogleIdToken.Payload payload = verifyIdToken(idToken);

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Account account = accountRepository.findByEmailAndAccountType(email, 0)
            .orElseGet(() -> {
                Account newAccount = Account.builder()
                    .email(email)
                    .name(name)
                    .accountType(0)
                    .status(0)
                    .build();
                return accountRepository.save(newAccount);
            });

        String accessToken = jwtProvider.generateAccessToken(account.getId());
        String refreshToken = jwtProvider.generateRefreshToken(account.getId());

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    private GoogleIdToken.Payload verifyIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid ID Token");
            }

            return idToken.getPayload();

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify ID token", e);
        }
    }
}
