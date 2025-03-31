package com.highfive.meetu.domain.job.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.CloseType;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.EducationLevel;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.ExperienceLevel;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.SalaryCode;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.Status;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 채용 공고 엔티티
 *
 * 연관관계:
 * - JobPosting(N) : Company(1) - JobPosting이 주인, @JoinColumn 사용
 * - JobPosting(N) : Account(1) - JobPosting이 주인, @JoinColumn 사용
 * - JobPosting(N) : Location(1) - JobPosting이 주인, @JoinColumn 사용
 * - JobPosting(M) : JobCategory(N) - 다대다 관계, 중간 테이블 사용
 */
@Entity(name = "jobPosting")
@Table(
        indexes = {
                @Index(name = "idx_jobPosting_companyId", columnList = "companyId"),
                @Index(name = "idx_jobPosting_locationCode", columnList = "locationCode"),
                @Index(name = "idx_jobPosting_status", columnList = "status"),
                @Index(name = "idx_jobPosting_expirationDate", columnList = "expirationDate")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"company", "businessAccount", "location", "jobCategories"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JobPosting extends BaseEntity {

    @Column(length = 50, unique = true)
    private String jobId;  // 외부 API 공고 ID (예: 사람인 job.id) / 자체 등록 공고는 NULL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;  // 기업 ID (공고를 등록한 회사)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessAccountId")
    private Account businessAccount;  // 채용 담당자 계정 ID (NULL 가능)

    @Column(length = 255, nullable = false)
    private String title;  // 공고 제목

    @Column(length = 500)
    private String jobUrl;  // 공고 상세 페이지 URL

    @Column(length = 255, nullable = false)
    private String industry;  // 산업 분야명

    @Column(length = 255, nullable = false)
    private String jobType;  // 근무 형태 (정규직, 계약직 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationCode", referencedColumnName = "locationCode", nullable = false)
    private Location location;  // 근무 지역 코드

    @Column(nullable = false)
    private ExperienceLevel experienceLevel;  // 경력 코드 (NEW_GRAD, JUNIOR, MID_LEVEL, SENIOR) - 컨버터 자동 적용

    @Column(nullable = false)
    private EducationLevel educationLevel;  // 학력 코드 - Enum 타입으로 변경

    @Column(nullable = false)
    private SalaryCode salaryCode;  // 연봉 코드 - Enum 타입으로 변경

    @Column(length = 255, nullable = false)
    private String salaryRange;  // 연봉 범위 텍스트 (예: "3,000~4,000만원")

    @Column(nullable = false)
    private LocalDateTime postingDate;  // 공고 게시일

    @Column
    private LocalDateTime openingDate;  // 공고 접수 시작일

    @Column(nullable = false)
    private LocalDateTime expirationDate;  // 공고 마감일

    @Column(nullable = false)
    private CloseType closeType;  // 마감 형식 (DEADLINE, UPON_HIRING) - 컨버터 자동 적용

    @Column(nullable = false)
    private Integer viewCount;  // 공고 조회수

    @Column(nullable = false)
    private Integer applyCount;  // 공고 지원자 수

    @Column(columnDefinition = "TEXT")
    private String keyword;  // 공고 키워드 (쉼표로 구분된 문자열)

    @Column
    private Integer templateType;  // 공고 템플릿 유형

    @Column(columnDefinition = "TEXT")
    private String description;  // 커스텀 공고 상세 설명

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 공고 수정일

    @Column(nullable = false)
    private Status status;  // 공고 상태 (INACTIVE, PENDING, ACTIVE) - 컨버터 자동 적용

    @BatchSize(size = 20)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "jobPostingJobCategory",
            joinColumns = @JoinColumn(name = "jobPostingId"),
            inverseJoinColumns = @JoinColumn(name = "jobCategoryId")
    )
    @JsonIgnoreProperties({"jobPostings"})
    @Builder.Default
    private List<JobCategory> jobCategories = new ArrayList<>();

    /**
     * 공고 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 공고가 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    /**
     * 공고가 만료되었는지 확인
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationDate);
    }

    /**
     * 조회수 증가
     */
    public void incrementViewCount() {
        this.viewCount++;
    }

    /**
     * 지원자 수 증가
     */
    public void incrementApplyCount() {
        this.applyCount++;
    }

    /**
     * 직무 카테고리 추가
     */
    public void addJobCategory(JobCategory category) {
        this.jobCategories.add(category);
        category.getJobPostings().add(this);
    }

    /**
     * 직무 카테고리 제거
     */
    public void removeJobCategory(JobCategory category) {
        this.jobCategories.remove(category);
        category.getJobPostings().remove(this);
    }
}