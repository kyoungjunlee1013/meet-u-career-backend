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
    private Integer status;  // 공고 상태 (DRAFT, PENDING, REJECTED, APPROVED, ACTIVE, INACTIVE)


    @BatchSize(size = 20)
    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"jobPosting"})
    @Builder.Default
    private List<JobPostingJobCategory> jobPostingJobCategoryList = new ArrayList<>();

    /**
     * 학력 수준 코드 (사람인 기준)
     */
    public static class EducationLevel {
        public static final int ANY = 0;              // 학력무관
        public static final int HIGH_SCHOOL = 1;      // 고등학교졸업
        public static final int COLLEGE = 2;          // 대학졸업(2,3년)
        public static final int UNIVERSITY = 3;       // 대학교졸업(4년)
        public static final int MASTER = 4;           // 석사졸업
        public static final int DOCTOR = 5;           // 박사졸업
        public static final int HIGH_SCHOOL_OR_MORE = 6;   // 고등학교졸업이상
        public static final int COLLEGE_OR_MORE = 7;       // 대학졸업(2,3년)이상
        public static final int UNIVERSITY_OR_MORE = 8;    // 대학교졸업(4년)이상
        public static final int MASTER_OR_MORE = 9;        // 석사졸업이상
    }

    /**
     * 희망 연봉 코드 (사람인 연동 기준)
     */
    public static class SalaryCode {
        public static final int NEGOTIABLE = 0;             // 회사 내규에 따름
        public static final int ABOVE_2600 = 9;             // 2,600만원 이상
        public static final int ABOVE_2800 = 10;            // 2,800만원 이상
        public static final int ABOVE_3000 = 11;            // 3,000만원 이상
        public static final int ABOVE_3200 = 12;            // 3,200만원 이상
        public static final int ABOVE_3400 = 13;            // 3,400만원 이상
        public static final int ABOVE_3600 = 14;            // 3,600만원 이상
        public static final int ABOVE_3800 = 15;            // 3,800만원 이상
        public static final int ABOVE_4000 = 16;            // 4,000만원 이상
        public static final int ABOVE_5000 = 17;            // 5,000만원 이상
        public static final int ABOVE_6000 = 18;            // 6,000만원 이상
        public static final int ABOVE_7000 = 19;            // 7,000만원 이상
        public static final int RANGE_8000_9000 = 20;       // 8,000~9,000만원
        public static final int RANGE_9000_10000 = 21;      // 9,000~1억원
        public static final int ABOVE_10000 = 22;           // 1억원 이상
        public static final int INTERVIEW = 99;             // 면접 후 결정
        public static final int MONTHLY = 101;              // 월급
        public static final int WEEKLY = 102;               // 주급
        public static final int DAILY = 103;                // 일급
        public static final int HOURLY = 104;               // 시급
        public static final int PER_PROJECT = 105;          // 건당
    }

    /**
     * 경력 코드 (사람인 기준)
     * - 채용 공고 조건 또는 사용자 프로필의 경력 유형 표현
     */
    public static class ExperienceCode {
        public static final int NEW = 1;          // 신입
        public static final int EXPERIENCED = 2;  // 경력
        public static final int BOTH = 3;         // 신입/경력
    }

    public static class CloseType {
        public static final int DEADLINE = 1;
        public static final int UNTIL_HIRE = 2;
    }

    public static class Status {

        public static final int DRAFT = 0;        // 임시 저장 (작성 완료, 아직 미제출)
        public static final int PENDING = 1;      // 승인 대기 (기업이 제출 → 관리자 검토 중)
        public static final int REJECTED = 2;     // 반려됨 (관리자가 승인 거절)

        public static final int APPROVED = 3;     // 승인 완료 (게시 예정 상태)
        public static final int ACTIVE = 4;       // 게시 중 (현재 날짜가 게시 시작~마감 사이)
        public static final int INACTIVE = 5;     // 게시 종료 (마감일 지나거나 수동 종료)

    }


}