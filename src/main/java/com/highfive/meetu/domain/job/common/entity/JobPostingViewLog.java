package com.highfive.meetu.domain.job.common.entity;

import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 채용 공고 조회 로그 엔티티
 *
 * 연관관계:
 * - Profile(1) : JobPostingViewLog(N) - JobPostingViewLog가 주인, @JoinColumn 사용
 * - JobPosting(1) : JobPostingViewLog(N) - JobPostingViewLog가 주인, @JoinColumn 사용
 */
@Entity(name = "jobPostingViewLog")
@Table(
        indexes = {
                @Index(name = "idx_job_view_profileId", columnList = "profileId"),
                @Index(name = "idx_job_view_jobPostingId", columnList = "jobPostingId"),
                @Index(name = "idx_job_view_createdAt", columnList = "createdAt")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"profile", "jobPosting"})
public class JobPostingViewLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;  // 조회한 개인회원의 프로필

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPostingId", nullable = false)
    private JobPosting jobPosting;  // 조회한 채용 공고
}