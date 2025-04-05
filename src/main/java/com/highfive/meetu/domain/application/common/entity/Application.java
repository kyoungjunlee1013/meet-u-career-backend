package com.highfive.meetu.domain.application.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity(name = "application")
@Table(
    indexes = {
        @Index(name = "idx_application_profileId", columnList = "profileId"),
        @Index(name = "idx_application_jobPostingId", columnList = "jobPostingId"),
        @Index(name = "idx_application_resumeId", columnList = "resumeId")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"profile", "jobPosting", "resume"}) //?????
@JsonIgnoreProperties()
public class Application extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profileId", nullable = false)//why nullable?
  private Profile profile; // 지원자 프로필

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "jobPostingId", nullable = false)
  private JobPosting jobPosting; // 채용 공고

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resumeId", nullable = false)
  private Resume resume; // 제출한 이력서

  @Column(nullable = false)
  private Integer status = 0; //지원한 상태 (0: 대기, 1: 검토 중, ...)

  // 지원 상태 코드 업데이트 메서드 (선택사항)
  public void updateStatus(Integer status) {
    this.status = status;
  }
}