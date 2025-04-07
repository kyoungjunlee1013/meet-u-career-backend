package com.highfive.meetu.infra.oauth;

import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final ProfileRepository profileRepository;

    /**
     * 현재 인증된 사용자의 accountId 반환
     * 인증되지 않은 경우 NotFoundException 발생
     */
    public Long getCurrentAccountId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (Long) Optional.ofNullable(auth)
            .map(Authentication::getPrincipal)
            .filter(principal -> principal instanceof Long)
            .map(Long.class::cast)
            .orElseThrow(() -> new NotFoundException("인증 정보가 없습니다."));
    }

    /**
     * 인증된 accountId를 기반으로 해당 사용자의 profileId 반환
     * 프로필이 존재하지 않으면 NotFoundException 발생
     */
    public Long getProfileIdByAccountId() {
        Long accountId = getCurrentAccountId();

        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new NotFoundException("해당 accountId에 해당하는 프로필을 찾을 수 없습니다."));

        return profile.getId();
    }

    /**
     * 인증 정보가 있을 경우 accountId 반환, 없으면 Optional.empty()
     * -> 메인 페이지 등 로그인 필수가 아닌 곳에서 사용
     */
    public Optional<Long> getOptionalAccountId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(auth)
            .map(Authentication::getPrincipal)
            .filter(p -> p instanceof Long)
            .map(Long.class::cast);
    }
}
