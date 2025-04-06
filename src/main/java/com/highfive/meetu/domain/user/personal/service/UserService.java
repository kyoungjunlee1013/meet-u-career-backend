package com.highfive.meetu.domain.user.personal.service;

import com.highfive.meetu.domain.auth.personal.dto.UserInfoDTO;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public ResultData<UserInfoDTO> getMyInfo(Long userId) {
        return accountRepository.findById(userId)
            .map(account -> ResultData.success(1, UserInfoDTO.from(account)))
            .orElse(ResultData.fail("사용자 정보를 찾을 수 없습니다."));
    }
}
