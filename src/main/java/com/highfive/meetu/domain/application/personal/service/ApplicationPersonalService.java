package com.highfive.meetu.domain.application.personal.service;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.repository.ApplicationQueryRepository;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.application.personal.dto.ApplicationPersonalDTO;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 개인 회원용 Application(지원서) Service
 */
@Service
@RequiredArgsConstructor
public class ApplicationPersonalService {

  private final ApplicationQueryRepository applicationQueryRepository;
  private final ApplicationRepository applicationRepository;

  /**
   * 특정 profileId의 지원 내역 목록 조회
   */
  public List<ApplicationPersonalDTO> getMyApplications(Long profileId) {
    // profileId가 실제 존재하는지 먼저 확인 (선택적)
    // 굳이 필요 없다면 생략 가능
    /*
     * boolean existsProfile = profileRepository.existsById(profileId);
     * if(!existsProfile) {
     * throw new NotFoundException("해당 프로필을 찾을 수 없습니다.");
     * }
     */

    // QueryDSL 통해 조회
    return applicationQueryRepository
        .findAllByProfileId(profileId)
        .stream()
        .filter(dto -> dto.getStatus() != Application.Status.DELETED)
        .toList();
  }

  /**
   * 특정 profileId와 status에 따른 지원 내역 목록 조회
   */
  public List<ApplicationPersonalDTO> getApplicationsByProfileIdAndStatus(Long profileId, Integer status) {
    return applicationQueryRepository.findAllByProfileId(profileId)
        .stream()
        .filter(dto -> (status == null || dto.getStatus().equals(status)))
        .filter(dto -> dto.getStatus() != Application.Status.DELETED)
        .toList();
  }

  /**
   * 지원 내역 취소
   */
  @Transactional
  public void cancelApplication(Long applicationId, Long profileId) {
    var app = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new NotFoundException("지원 내역이 존재하지 않습니다."));

    if (!app.getProfile().getId().equals(profileId)) {
      throw new NotFoundException("해당 사용자의 지원이 아닙니다.");
    }

    app.setStatus(Application.Status.DELETED);
    applicationRepository.save(app);
  }

}
