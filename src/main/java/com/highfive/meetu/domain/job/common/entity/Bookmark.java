package com.highfive.meetu.domain.job.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 채용 공고 북마크 엔티티
 *
 * 연관관계:
 * - Bookmark(N) : Account(1) - Bookmark가 주인, @JoinColumn 사용
 * - Bookmark(N) : JobPosting(1) - Bookmark가 주인, @JoinColumn 사용
 */
@Entity(name = "bookmark")
@Table(
        indexes = {
                @Index(name = "idx_bookmark_accountId", columnList = "accountId"),
                @Index(name = "idx_bookmark_jobPostingId", columnList = "jobPostingId")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_bookmark_account_jobPosting", columnNames = {"accountId", "jobPostingId"})
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "jobPosting"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bookmark extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;  // 북마크한 사용자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPostingId", nullable = false)
    private JobPosting jobPosting;  // 북마크한 채용 공고 ID
}