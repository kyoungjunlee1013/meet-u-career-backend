package com.highfive.meetu.domain.job.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.company.common.entity.Company;
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
@ToString(exclude = {"company", "businessAccount", "location", "jobPostingJobCategoryList"})
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
    private Integer experienceLevel;  // 경력 코드 (NEW_GRAD, JUNIOR, MID_LEVEL, SENIOR)

    @Column(nullable = false)
    private Integer educationLevel;  // 학력 코드 - Enum 타입으로 변경

    @Column(nullable = false)
    private Integer salaryCode;  // 연봉 코드 - Enum 타입으로 변경

    @Column(length = 255, nullable = false)
    private String salaryRange;  // 연봉 범위 텍스트 (예: "3,000~4,000만원")

    @Column(nullable = false)
    private LocalDateTime postingDate;  // 공고 게시일

    @Column
    private LocalDateTime openingDate;  // 공고 접수 시작일

    @Column(nullable = false)
    private LocalDateTime expirationDate;  // 공고 마감일

    @Column(nullable = false)
    private Integer closeType;  // 마감 형식 (DEADLINE, UPON_HIRING)

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
    private Integer status;  // 공고 상태 (INACTIVE, PENDING, ACTIVE)

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"jobPosting"})
    @Builder.Default
    private List<JobPostingJobCategory> jobPostingJobCategoryList = new ArrayList<>();

    public static class EducationLevel {
        public static final int NONE = 0;
        public static final int HIGH_SCHOOL = 1;
        public static final int ASSOCIATE = 2;
        public static final int BACHELOR = 3;
        public static final int MASTER = 4;
        public static final int DOCTOR = 5;
    }

    public static class SalaryCode {
        public static final int NEGOTIABLE = 0;
        public static final int ABOVE_2600 = 9;
        public static final int ABOVE_2800 = 10;
        public static final int ABOVE_3000 = 11;
        public static final int ABOVE_3200 = 12;
        public static final int ABOVE_3400 = 13;
        public static final int ABOVE_3600 = 14;
        public static final int ABOVE_3800 = 15;
        public static final int ABOVE_4000 = 16;
        public static final int ABOVE_5000 = 17;
        public static final int ABOVE_6000 = 18;
        public static final int ABOVE_7000 = 19;
        public static final int RANGE_8000_9000 = 20;
        public static final int RANGE_9000_10000 = 21;
        public static final int ABOVE_10000 = 22;
        public static final int INTERVIEW = 99;
        public static final int MONTHLY = 101;
        public static final int WEEKLY = 102;
        public static final int DAILY = 103;
        public static final int HOURLY = 104;
        public static final int PER_PROJECT = 105;
    }

    public static class ExperienceLevel {
        public static final int NEW_GRAD = 0;
        public static final int JUNIOR = 1;
        public static final int MID_LEVEL = 2;
        public static final int SENIOR = 3;
    }

    public static class CloseType {
        public static final int DEADLINE = 1;
        public static final int UNTIL_HIRE = 2;
    }

    public static class Status {
        public static final int INACTIVE = 0;
        public static final int PENDING = 1;
        public static final int ACTIVE = 2;
    }

}