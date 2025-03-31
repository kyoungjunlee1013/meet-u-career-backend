package com.highfive.meetu.domain.cs.common.entity;

import com.highfive.meetu.domain.cs.common.type.CustomerSupportTypes.Category;
import com.highfive.meetu.domain.cs.common.type.CustomerSupportTypes.Status;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.entity.Admin;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 고객센터 문의 엔티티
 *
 * 연관관계:
 * - Account(1) : CustomerSupport(N) - CustomerSupport가 주인, @JoinColumn 사용
 * - Admin(1) : CustomerSupport(N) - CustomerSupport가 주인, @JoinColumn 사용
 */
@Entity(name = "customerSupport")
@Table(
        indexes = {
                @Index(name = "idx_cs_accountId", columnList = "accountId"),
                @Index(name = "idx_cs_adminId", columnList = "adminId"),
                @Index(name = "idx_cs_category", columnList = "category"),
                @Index(name = "idx_cs_status", columnList = "status"),
                @Index(name = "idx_cs_createdAt", columnList = "createdAt")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "admin"})
public class CustomerSupport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;  // 문의를 보낸 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private Admin admin;  // 답변을 담당하는 관리자 (NULL 가능)

    @Column(nullable = false)
    private Category category;  // 문의 카테고리 - 컨버터 자동 적용

    @Column(length = 255, nullable = false)
    private String title;  // 문의 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;  // 문의 상세 내용

    @Column(columnDefinition = "TEXT")
    private String response;  // 관리자의 답변 (NULL 가능)

    @Column(nullable = false)
    private Status status;  // 상태 (PENDING, RESOLVED) - 컨버터 자동 적용

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 문의 상태 변경일

    /**
     * 문의 상태 업데이트
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 문의가 처리됐는지 확인
     */
    public boolean isResolved() {
        return this.status == Status.RESOLVED;
    }

    /**
     * 관리자 할당 및 상태 업데이트
     */
    public void assignAdmin(Admin admin) {
        this.admin = admin;
        if (this.status == Status.PENDING) {
            this.status = Status.IN_PROGRESS;
        }
    }

    /**
     * 답변 설정
     */
    public void setResponseAndResolve(String response) {
        this.response = response;
        this.status = Status.RESOLVED;
    }
}