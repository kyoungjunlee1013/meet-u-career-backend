package com.highfive.meetu.infra.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.highfive.meetu.global.common.exception.UnauthorizedException; // UnauthorizedException

// public class SecurityUtil {
//     public static Long getCurrentAccountId() {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//         if (auth != null && auth.getPrincipal() instanceof Long) { // accountId가 Long으로 반환되는 경우
//             return (Long) auth.getPrincipal(); // 바로 accountId를 반환
//         }
//
//         throw new UnauthorizedException("인증 정보가 없습니다.");
//     }
// }

import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.UnauthorizedException; // UnauthorizedException
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final ProfileRepository profileRepository;

    // 현재 인증된 사용자의 accountId를 가져오는 메서드
    public static Long getCurrentAccountId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Long) { // accountId가 Long으로 반환되는 경우
            return (Long) auth.getPrincipal(); // 바로 accountId를 반환
        }

        throw new UnauthorizedException("인증 정보가 없습니다.");
    }

    // 현재 인증된 사용자의 accountId로 Profile 테이블에서 id를 가져오는 메서드
    public Long getProfileIdByAccountId() {
        // accountId 가져오기
        Long accountId = getCurrentAccountId();

        // Profile 테이블에서 accountId에 해당하는 프로필 조회
        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new UnauthorizedException("해당 accountId에 해당하는 프로필을 찾을 수 없습니다."));

        // 프로필 id 반환
        return profile.getId();
    }
}
