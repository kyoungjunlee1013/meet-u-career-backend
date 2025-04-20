package com.highfive.meetu.domain.auth.personal.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class GoogleUserResponse {
    private String email;
    private String name;
    private String picture;
}
