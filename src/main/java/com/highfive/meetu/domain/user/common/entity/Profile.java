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
    private String profileImageUrl;  // 프로필 이미지 URL

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"profile"})
    @Builder.Default
    private List<Resume> resumeList = new ArrayList<>();

    /**
     * 경력 수준 코드
     */
    public static class ExperienceLevel {
        public static final int NEW_GRAD = 0;     // 신입
        public static final int JUNIOR = 1;       // 1~3년차
        public static final int MID_LEVEL = 2;    // 4~7년차
        public static final int SENIOR = 3;       // 8년 이상
    }

    /**
     * 학력 수준 코드
     */
    public static class EducationLevel {
        public static final int HIGH_SCHOOL = 0;
        public static final int ASSOCIATE = 1;
        public static final int BACHELOR = 2;
        public static final int MASTER = 3;
        public static final int DOCTORAL = 4;
    }
}