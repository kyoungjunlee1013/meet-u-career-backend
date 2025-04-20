package com.highfive.meetu.domain.auth.personal.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class NaverUserResponse {
    private NaverAccount response;

    @Getter
    @NoArgsConstructor
    public static class NaverAccount {
        private String email;
        private String name;
        private String nickname;
        private String profile_image;
        private String id;
    }
}
