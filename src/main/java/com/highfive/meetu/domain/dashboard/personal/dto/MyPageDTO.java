package com.highfive.meetu.domain.dashboard.personal.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageDTO {

  private AccountDTO account;
  private ProfileDTO profile;
  private int resumeViewCount;
  private int offerCount;
  private int bookmarkCount;
  private int applicationCount; // ✅ 지원 현황 필드
  private List<RecentApplicationDTO> recentApplications;
  private ApplicationSummaryDTO summary;
  private List<RecommendedJobPostingDTO> recommendedJobs;
  private int profileCompleteness;

  public static MyPageDTO of(AccountDTO account,
                             ProfileDTO profile,
                             int resumeViewCount,
                             int offerCount,
                             int bookmarkCount,
                             int applicationCount, // ✅ 파라미터 포함
                             List<RecentApplicationDTO> recentApplications,
                             ApplicationSummaryDTO summary,
                             List<RecommendedJobPostingDTO> recommendedJobs,
                             int profileCompleteness) {
    return MyPageDTO.builder()
            .account(account)
            .profile(profile)
            .resumeViewCount(resumeViewCount)
            .offerCount(offerCount)
            .bookmarkCount(bookmarkCount)
            .applicationCount(applicationCount) // ✅ 매핑
            .recentApplications(recentApplications)
            .summary(summary)
            .recommendedJobs(recommendedJobs)
            .profileCompleteness(profileCompleteness)
            .build();
  }
}
