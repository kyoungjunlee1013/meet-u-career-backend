package com.highfive.meetu.domain.community.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.community.common.type.CommunityTagTypes.Status;
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
@ToString(exclude = {"posts"})
public class CommunityTag extends BaseEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String name;  // 태그명

    @Column(nullable = false)
    private Status status;  // 태그 상태 (ACTIVE, INACTIVE) - 컨버터 자동 적용

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
    private List<CommunityPost> posts = new ArrayList<>();

    /**
     * 태그 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 태그가 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }
}