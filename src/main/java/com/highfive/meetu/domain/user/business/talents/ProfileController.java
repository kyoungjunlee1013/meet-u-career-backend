package com.highfive.meetu.domain.user.business.talents;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.domain.user.common.service.ProfileService;
import com.highfive.meetu.domain.user.business.talents.ProfileDto;

/**
 * 프로필 관련 REST API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/business/talents")
public class ProfileController {

    private final ProfileService profileService;

    // 프로필 목록 조회
    @GetMapping
    public ResultData<List<ProfileDto>> getAllProfiles() {
        List<ProfileDto> list = profileService.findAll();
        return ResultData.success(list.size(), list);
    }

    // 프로필 단건 조회
    @GetMapping("/{id}")
    public ResultData<ProfileDto> getProfile(@PathVariable Long id) {
        ProfileDto dto = profileService.findById(id);
        return ResultData.success(1, dto);
    }

    // 프로필 생성
    @PostMapping
    public ResultData<ProfileDto> createProfile(@Valid @RequestBody ProfileDto dto) {
        ProfileDto created = profileService.create(dto);
        return ResultData.success(1, created);
    }

    // 프로필 수정
    @PutMapping("/{id}")
    public ResultData<ProfileDto> updateProfile(@PathVariable Long id, @Valid @RequestBody ProfileDto dto) {
        ProfileDto updated = profileService.update(id, dto);
        return ResultData.success(1, updated);
    }

    // 프로필 삭제
    @DeleteMapping("/{id}")
    public ResultData<Void> deleteProfile(@PathVariable Long id) {
        profileService.delete(id);
        return ResultData.success(0, null);
    }
}
