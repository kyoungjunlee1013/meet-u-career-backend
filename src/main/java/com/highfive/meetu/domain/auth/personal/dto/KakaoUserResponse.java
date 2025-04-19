package com.highfive.meetu.domain.auth.personal.dto;

import lombok.*;

@Getter
@Setter
public class KakaoUserResponse {
    private Long id;
    private KakaoAccount kakao_account;

    @Getter @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter @Setter
        public static class Profile {
            private String nickname;
        }
    }
}
