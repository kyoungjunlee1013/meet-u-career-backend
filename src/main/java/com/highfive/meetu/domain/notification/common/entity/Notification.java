package com.highfive.meetu.domain.notification.common.entity;

import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 알림 엔티티
 *
 * 연관관계:
 * - Account(1) : Notification(N) - Notification이 주인, @JoinColumn 사용
 */
@Entity(name = "notification")
@Table(
        indexes = {
                @Index(name = "idx_notification_accountId", columnList = "accountId"),
                @Index(name = "idx_notification_type", columnList = "notificationType"),
                @Index(name = "idx_notification_isRead", columnList = "isRead")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account"})
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;  // 알림을 받는 사용자

    @Column(nullable = false)
    private Integer notificationType;  // 알림 유형 - INT 타입으로 변경

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;  // 알림 메시지 내용

    @Column(nullable = false)
    private Long relatedId;  // 관련된 데이터 ID

    @Column(nullable = false)
    private Integer isRead;  // 읽음 여부

    /**
     * 알림 타입
     */
    public static class NotificationType {

        // 개인회원 알림
        public static final int APPLICATION_STATUS = 1;         // 지원 상태 변경
        public static final int INTERVIEW_SCHEDULE = 2;         // 면접 일정 알림
        public static final int JOB_RECOMMENDATION = 3;         // 공고 추천
        public static final int OFFER_RECEIVED = 4;             // 제안 수신
        public static final int MESSAGE_RECEIVED = 5;           // 채팅 메시지 도착

        // 기업회원 알림
        public static final int NEW_APPLICATION = 11;           // 새로운 지원서
        public static final int APPLICATION_WITHDRAWN = 12;     // 지원서 철회
        public static final int JOB_POSTING_EXPIRING = 13;      // 공고 마감 임박
        public static final int PAYMENT_NOTIFICATION = 14;      // 결제 관련 알림
        public static final int TALENT_SUGGESTION = 15;         // 인재 추천

        // 공통 알림
        public static final int SYSTEM_NOTIFICATION = 21;       // 시스템 알림
        public static final int ACCOUNT_SECURITY = 22;          // 보안 관련 알림
    }


    /**
     * 읽음 여부
     */
    public static class ReadStatus {
        public static final int UNREAD = 0;
        public static final int READ = 1;
    }
}