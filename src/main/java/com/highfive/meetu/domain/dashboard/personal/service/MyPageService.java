package com.highfive.meetu.domain.dashboard.personal.service;

import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.company.common.repository.CompanyRepository;
import com.highfive.meetu.domain.dashboard.personal.dto.*;
import com.highfive.meetu.domain.job.common.repository.BookmarkRepository;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.offer.common.repository.OfferRepository;
import com.highfive.meetu.domain.resume.common.repository.ResumeRepository;
import com.highfive.meetu.domain.resume.common.repository.ResumeViewLogRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final AccountRepository accountRepository;
  private final ProfileRepository profileRepository;
  private final ResumeRepository resumeRepository;
  private final ResumeViewLogRepository resumeViewLogRepository;
  private final OfferRepository offerRepository;
  private final BookmarkRepository bookmarkRepository;
  private final ApplicationRepository applicationRepository;
  private final JobPostingRepository jobPostingRepository;
  private final CompanyRepository companyRepository;

  public MyPageDTO getMyPageInfo(Long accountId) {

    // 1. 기본 정보 조회
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new NotFoundException("계정을 찾을 수 없습니다."));
    Profile profile = profileRepository.findByAccountId(accountId)
        .orElseThrow(() -> new NotFoundException("프로필을 찾을 수 없습니다."));

    // 2. 수치 데이터 조회
    int resumeViewCount = resumeViewLogRepository.countByProfileId(profile.getId());
    int offerCount = offerRepository.countByPersonalAccountId(accountId);
    int bookmarkCount = bookmarkRepository.countByAccountId(accountId);

    // 3. 최근 지원 목록
    List<RecentApplicationDTO> recentApplications = applicationRepository.findRecentByProfileId(profile.getId());

    // 4. 총 지원 요약
    ApplicationSummaryDTO summary = applicationRepository.aggregateStatusSummary(profile.getId());

    // 5. 추천 공고
    List<RecommendedJobPostingDTO> recommendedJobs = jobPostingRepository.findRecommendedForProfile(profile, PageRequest.of(0, 6));



    // 6. 프로필 완성도 계산 (Account의 5개 항목: 이름, 이메일, 전화번호, 생년월일, 주소정보 각 20%)
    int profileCompleteness = 0;
    if (StringUtils.hasText(account.getName())) {
      profileCompleteness += 20;
    }
    if (StringUtils.hasText(account.getEmail())) {
      profileCompleteness += 20;
    }
    if (StringUtils.hasText(account.getPhone())) {
      profileCompleteness += 20;
    }
    if (account.getBirthday() != null) {
      profileCompleteness += 20;
    }
    if (profile.getLocation() != null && StringUtils.hasText(profile.getLocation().getFullLocation())) {
      profileCompleteness += 20;
    }



    // 7. DTO 조립 (프로필 완성도 값을 포함)
    return MyPageDTO.of(
        AccountDTO.from(account),
        ProfileDTO.from(profile),
        resumeViewCount,
        offerCount,
        bookmarkCount,
        recentApplications,
        summary,
        recommendedJobs,
        profileCompleteness
    );
  }
}
