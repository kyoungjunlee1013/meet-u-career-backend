package com.highfive.meetu.global.util;

import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestUtil {
    private final ProfileRepository profileRepository;

    // accountId를 가져오는 메서드
    public static Long getAccountId(HttpServletRequest request) {
        Object accountId = request.getAttribute("userId");

        if (accountId instanceof Long) {
            return (Long) accountId;
        } else if (accountId instanceof Integer) {
            return ((Integer) accountId).longValue();
        }

        throw new NotFoundException("요청에서 accountId를 찾을 수 없습니다.");
    }

    // accountId로 profile의 id를 가져오는 메서드
    public Long getProfileIdByAccountId(HttpServletRequest request) {
        Long accountId = getAccountId(request);

        Profile profile = profileRepository.findByAccountId(accountId)
            .orElseThrow(() -> new NotFoundException("해당 accountId에 해당하는 프로필을 찾을 수 없습니다."));

        return profile.getId();
    }
}
