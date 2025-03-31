package com.highfive.meetu.domain.job.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 지역 엔티티
 *
 * 연관관계:
 * - Location(1) : Profile(N) - Location이 비주인, mappedBy 사용
 * - Location(1) : JobPosting(N) - Location이 비주인, mappedBy 사용
 */
@Entity(name = "location")
@Table(
        indexes = {
                @Index(name = "idx_location_locationCode", columnList = "locationCode")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"profileList", "jobPostingList"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location extends BaseEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String locationCode;  // 지역 코드 (API 연동 가능)

    @Column(length = 100, nullable = false)
    private String province;  // 광역시/도 (예: 서울특별시, 경기도 등)

    @Column(length = 100)
    private String city;  // 시/군/구 (예: 성남시 분당구, 강남구 등)

    @Column(length = 255, nullable = false)
    private String fullLocation;  // 전체 지역명 (예: "경기도 성남시 분당구")

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 지역 데이터 수정일

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"location"})
    @Builder.Default
    private List<Profile> profileList = new ArrayList<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"location"})
    @Builder.Default
    private List<JobPosting> jobPostingList = new ArrayList<>();
}