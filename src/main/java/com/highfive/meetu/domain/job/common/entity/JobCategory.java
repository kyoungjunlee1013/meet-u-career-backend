package com.highfive.meetu.domain.job.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 * 직무 코드 엔티티
 *
 * 연관관계:
 * - JobCategory(1) : JobCategory(N) - 자기 참조 관계
 * - JobCategory(N) : JobPosting(M) - 다대다 관계, 중간 테이블 사용
 */
@Entity(name = "jobCategory")
@Table(
        indexes = {
                @Index(name = "idx_jobCategory_jobCode", columnList = "jobCode"),
                @Index(name = "idx_jobCategory_parentCode", columnList = "parentCode")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"childCategories", "jobPostings"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JobCategory extends BaseEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String jobCode;  // 사람인 API의 직무 코드 값

    @Column(length = 255, nullable = false)
    private String jobName;  // 사람인 API의 직무명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentCode", referencedColumnName = "jobCode")
    private JobCategory parentCategory;  // 상위 카테고리

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"parentCategory"})
    @Builder.Default
    private List<JobCategory> childCategoryList = new ArrayList<>();

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "jobCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"jobCategory"})
    @Builder.Default
    private List<JobPostingJobCategory> jobPostingJobCategoryList = new ArrayList<>();

}