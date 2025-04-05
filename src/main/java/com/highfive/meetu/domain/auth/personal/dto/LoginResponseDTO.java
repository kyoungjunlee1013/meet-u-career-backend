package com.highfive.meetu.domain.auth.personal.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
