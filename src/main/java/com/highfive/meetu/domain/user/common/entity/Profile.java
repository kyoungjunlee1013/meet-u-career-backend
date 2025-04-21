package com.highfive.meetu.domain.user.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.job.common.entity.JobCategory;
import com.highfive.meetu.domain.job.common.entity.Location;
import com.highfive.meetu.domain.resume.common.entity.Resume;
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
 * 구직자 프로필 엔티티
 *
 * 연관관계:
 * - Profile(1) : Account(1) - Profile이 주인, @JoinColumn 사용
 * - Profile(N) : Location(1) - Profile이 주인, @JoinColumn 사용
 * - Profile(N) : JobCategory(1) - 희망 직무 (nullable)
 * - Profile(1) : Resume(N) - Profile이 비주인, mappedBy 사용
 */
@Entity(name = "profile")
@Table(
        indexes = {
                @Index(name = "idx_profile_accountId", columnList = "accountId"),
                @Index(name = "idx_profile_locationId", columnList = "locationId"),
                @Index(name = "idx_profile_desiredJobCategoryId", columnList = "desiredJobCategoryId")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "location", "desiredLocation", "desiredJobCategory", "resumeList"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Profile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false, unique = true)
    private Account account;  // 계정 ID (각 개인 계정은 1개의 프로필만 가짐)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId", nullable = true)
    private Location location;  // 거주 지역 (선택)

    @Column
    private Integer experienceLevel;  // 경력 수준

    @Column
    private Integer educationLevel;  // 학력 수준

    @Column(columnDefinition = "TEXT")
    private String skills;  // 보유 기술

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desiredJobCategoryId")
    private JobCategory desiredJobCategory;  // 희망 직무 (nullable, FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desiredLocationId")
    private Location desiredLocation;  // 희망 근무 지역 (nullable)

    @Column(name = "desiredSalaryCode")
    private Integer desiredSalaryCode;  // 희망 연봉 코드

    @Column(length = 500)
    private String profileImageKey;  // 프로필 이미지 URL

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"profile"})
    @Builder.Default
    private List<Resume> resumeList = new ArrayList<>();

    /**
     * 경력 코드 (사람인 기준)
     * - 채용 공고 조건 또는 사용자 프로필의 경력 유형 표현
     */
    public static class ExperienceCode {
        public static final int NEW = 1;          // 신입
        public static final int EXPERIENCED = 2;  // 경력
        public static final int BOTH = 3;         // 신입/경력
    }

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
    public static class DesiredSalaryCode {
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
}