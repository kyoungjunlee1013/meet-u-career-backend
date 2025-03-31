package com.highfive.meetu.domain.user.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.highfive.meetu.domain.job.common.entity.Location;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.EducationLevel;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.ExperienceLevel;
import com.highfive.meetu.domain.job.common.type.JobPostingTypes.SalaryCode;
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
 * - Profile(1) : Resume(N) - Profile이 비주인, mappedBy 사용
 */
@Entity(name = "profile")
@Table(
        indexes = {
                @Index(name = "idx_profile_accountId", columnList = "accountId"),
                @Index(name = "idx_profile_locationId", columnList = "locationId")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "location", "desiredLocation", "resumeList"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Profile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false, unique = true)
    private Account account;  // 계정 ID (각 개인 계정은 1개의 프로필만 가짐)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId", nullable = false)
    private Location location;  // 거주 지역 ID

    @Column
    private ExperienceLevel experienceLevel;  // 경력 수준 (예: 신입, 주니어, 미드레벨, 시니어)

    @Column
    private EducationLevel educationLevel;  // 학력 수준 (예: 학사, 석사 등)

    @Column(columnDefinition = "TEXT")
    private String skills;  // 보유 기술 (예: "Java, Spring, SQL")

    @Column(length = 50)
    private String desiredJob;  // 희망 직무 (예: "백엔드 개발자")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desiredLocationId")
    private Location desiredLocation;  // 희망 근무 지역 ID

    @Column(name = "desiredSalaryCode")
    private SalaryCode desiredSalaryCode;  // 희망 연봉 코드 (사람인 API 코드와 동일)

    @Column(length = 500)
    private String profileImageUrl;  // 프로필 이미지 URL

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 프로필 수정일

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"profile"})
    @Builder.Default
    private List<Resume> resumeList = new ArrayList<>();

    /**
     * 이력서 추가 편의 메서드
     */
    public void addResume(Resume resume) {
        this.resumeList.add(resume);
        resume.setProfile(this);
    }

    /**
     * 이력서 제거 편의 메서드
     */
    public void removeResume(Resume resume) {
        this.resumeList.remove(resume);
        resume.setProfile(null);
    }
}