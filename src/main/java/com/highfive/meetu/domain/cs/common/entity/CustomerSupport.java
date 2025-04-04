package com.highfive.meetu.domain.cs.common.entity;

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
    private Integer category;  // 문의 카테고리

    @Column(length = 255, nullable = false)
    private String title;  // 문의 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;  // 문의 상세 내용

    @Column(columnDefinition = "TEXT")
    private String response;  // 관리자의 답변 (NULL 가능)

    @Column(nullable = false)
    private Integer status;  // 상태 (PENDING, RESOLVED)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 문의 상태 변경일

    // 문의 카테고리 상수
    public static class Category {
        public static final int PAYMENT = 0;        // 결제
        public static final int SERVICE = 1;        // 서비스 이용
        public static final int ACCOUNT = 2;        // 계정
        public static final int JOB_POSTING = 3;    // 채용 공고
        public static final int COMPANY = 4;        // 기업 정보
        public static final int RESUME = 5;         // 이력서
        public static final int APPLICATION = 6;    // 지원서
        public static final int OTHER = 7;          // 기타
    }

    // 문의 상태 상수
    public static class Status {
        public static final int PENDING = 0;      // 접수됨
        public static final int IN_PROGRESS = 1;  // 처리 중
        public static final int RESOLVED = 2;     // 처리 완료
    }

}