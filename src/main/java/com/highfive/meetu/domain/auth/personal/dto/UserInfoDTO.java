package com.highfive.meetu.domain.auth.personal.dto;

import com.highfive.meetu.domain.user.common.entity.Account;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private Long id;
    private String name;
    private String email;
    private String profileImage;

    public static UserInfoDTO from(Account account) {
        return UserInfoDTO.builder()
            .id(account.getId())
            .name(account.getName())
            .email(account.getEmail())
            .profileImage(account.getCommunityProfileImageUrl())
            .build();
    }
}