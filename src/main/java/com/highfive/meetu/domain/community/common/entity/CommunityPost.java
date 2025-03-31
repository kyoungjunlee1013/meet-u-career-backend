package com.highfive.meetu.domain.community.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.community.common.type.CommunityPostTypes.Status;
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
 * 커뮤니티 게시글 엔티티
 *
 * 연관관계:
 * - Account(1) : CommunityPost(N) - CommunityPost가 주인, @JoinColumn 사용
 * - CommunityTag(1) : CommunityPost(N) - CommunityPost가 주인, @JoinColumn 사용
 * - CommunityPost(1) : CommunityComment(N) - CommunityPost가 비주인, mappedBy 사용
 * - CommunityPost(1) : CommunityLike(N) - CommunityPost가 비주인, mappedBy 사용
 */
@Entity(name = "communityPost")
@Table(
        indexes = {
                @Index(name = "idx_post_accountId", columnList = "accountId"),
                @Index(name = "idx_post_tagId", columnList = "tagId"),
                @Index(name = "idx_post_status", columnList = "status"),
                @Index(name = "idx_post_createdAt", columnList = "createdAt")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "tag", "comments", "likes"})
public class CommunityPost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;  // 게시글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId", nullable = false)
    private CommunityTag tag;  // 게시글의 태그

    @Column(length = 255, nullable = false)
    private String title;  // 게시글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;  // 게시글 내용

    @Column(nullable = false)
    private Integer likeCount;  // 좋아요 수

    @Column(nullable = false)
    private Integer commentCount;  // 댓글 수

    @Column(nullable = false)
    private Status status;  // 게시글 상태 (ACTIVE, DELETED) - 컨버터 자동 적용

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 게시글 수정일

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"post"})
    @Builder.Default
    private List<CommunityComment> comments = new ArrayList<>();

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties({"post"})
    @Builder.Default
    private List<CommunityLike> likes = new ArrayList<>();

    /**
     * 댓글 추가 편의 메서드
     */
    public void addComment(CommunityComment comment) {
        this.comments.add(comment);
        comment.setPost(this);
        this.commentCount++;
    }

    /**
     * 좋아요 추가 편의 메서드
     */
    public void addLike(CommunityLike like) {
        this.likes.add(like);
        like.setPost(this);
        this.likeCount++;
    }

    /**
     * 댓글 삭제 편의 메서드
     */
    public void removeComment(CommunityComment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
        if(this.commentCount > 0) {
            this.commentCount--;
        }
    }

    /**
     * 좋아요 삭제 편의 메서드
     */
    public void removeLike(CommunityLike like) {
        this.likes.remove(like);
        like.setPost(null);
        if(this.likeCount > 0) {
            this.likeCount--;
        }
    }

    /**
     * 게시글 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 게시글이 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }
}