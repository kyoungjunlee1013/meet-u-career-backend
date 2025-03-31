package com.highfive.meetu.domain.job.common.entity;

import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 공고-직무 관계 엔티티
 *
 * 연관관계:
 * - JobPosting(1) : JobPostingJobCategory(N) - JobPostingJobCategory가 주인, @JoinColumn 사용
 * - JobCategory(1) : JobPostingJobCategory(N) - JobPostingJobCategory가 주인, @JoinColumn 사용
 */
@Entity(name = "jobPostingJobCategory")
@Table(
        indexes = {
                @Index(name = "idx_jobPostingId", columnList = "jobPostingId"),
                @Index(name = "idx_jobCategoryId", columnList = "jobCategoryId")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_job_posting_category", columnNames = {"jobPostingId", "jobCategoryId"})
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"jobPosting", "jobCategory"})
public class JobPostingJobCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPostingId", nullable = false)
    private JobPosting jobPosting;  // 채용 공고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobCategoryId", nullable = false)
    private JobCategory jobCategory;  // 직무 카테고리
}