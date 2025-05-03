package com.highfive.meetu.domain.user.common.service;

import java.util.List;

import com.highfive.meetu.domain.user.business.dto.ProfileDto;

/**
 * 프로필 서비스 인터페이스
 */
public interface ProfileService {

    /**
     * 프로필 목록 조회
     * 
     * @return 프로필 DTO 리스트
     */
    List<ProfileDto> findAll();

    /**
     * 프로필 단건 조회
     * 
     * @param id 프로필 ID
     * @return 프로필 DTO
     */
    ProfileDto findById(Long id);

    /**
     * 프로필 생성
     * 
     * @param dto 생성용 프로필 DTO
     * @return 생성된 프로필 DTO
     */
    ProfileDto create(ProfileDto dto);

    /**
     * 프로필 수정
     * 
     * @param id  수정할 프로필 ID
     * @param dto 수정용 프로필 DTO
     * @return 수정된 프로필 DTO
     */
    ProfileDto update(Long id, ProfileDto dto);

    /**
     * 프로필 삭제
     * 
     * @param id 삭제할 프로필 ID
     */
    void delete(Long id);
}