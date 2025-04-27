package com.highfive.meetu.domain.auth.personal.dto;

import lombok.*;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String userId;
    private String password;
}
