package com.highfive.meetu.infra.oauth;

import com.highfive.meetu.global.common.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final ProfileRepository profileRepository;

    // 현재 인증된 사용자의 accountId를 가져오는 메서드
    public static Long getAccountId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 존재하고, principal이 Long 타입인 경우
        return (Long) Optional.ofNullable(auth)
            .map(Authentication::getPrincipal)  // 인증된 사용자 정보 가져오기
            .filter(principal -> principal instanceof Long) // principal이 Long 타입인지 확인
            .map(Long.class::cast) // principal을 Long으로 캐스팅
            .orElseThrow(() -> new NotFoundException("인증 정보가 없습니다.")); // 인증 정보가 없으면 NotFoundException 발생
    }

    // 현재 인증된 사용자의 accountId로 Profile 테이블에서 id를 가져오는 메서드
    public Long getProfileId() {
        Long accountId = getAccountId();

        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new NotFoundException("해당 accountId에 해당하는 프로필을 찾을 수 없습니다."));

        return profile.getId();
    }
}
