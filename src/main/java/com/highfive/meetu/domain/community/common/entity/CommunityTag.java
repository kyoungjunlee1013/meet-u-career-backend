package com.highfive.meetu.domain.community.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * 커뮤니티 태그 엔티티
 *
 * 연관관계:
 * - CommunityTag(1) : CommunityPost(N) - CommunityTag가 비주인, mappedBy 사용
 */
@Entity(name = "communityTag")
@Table(
        indexes = {
                @Index(name = "idx_community_tag_name", columnList = "name"),
                @Index(name = "idx_community_tag_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"postList"})
public class CommunityTag extends BaseEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String name;  // 태그명

    @Column(nullable = false)
    private Integer status;  // 태그 상태 (ACTIVE, INACTIVE)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 태그 수정일

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "tag",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"tag"})
    @Builder.Default
    private List<CommunityPost> postList = new ArrayList<>();

    // 상태 상수 정의
    public static class Status {
        public static final int ACTIVE = 0;    // 사용 중
        public static final int INACTIVE = 1;  // 비활성화됨
    }
}