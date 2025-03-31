package com.highfive.meetu.domain.notification.common.entity;

import com.highfive.meetu.domain.notification.common.type.NotificationTypes;
import com.highfive.meetu.domain.notification.common.type.NotificationTypes.NotificationType;
import com.highfive.meetu.domain.notification.common.type.NotificationTypes.ReadStatus;
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
    @Convert(converter = NotificationTypes.NotificationTypeConverter.class)
    private NotificationType notificationType;  // 알림 유형 - INT 타입으로 변경, 컨버터 명시적 적용

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;  // 알림 메시지 내용

    @Column(nullable = false)
    private Long relatedId;  // 관련된 데이터 ID

    @Column(nullable = false)
    @Convert(converter = NotificationTypes.ReadStatusConverter.class)
    private ReadStatus isRead;  // 읽음 여부 - 컨버터 명시적 적용

    /**
     * 알림을 읽음 상태로 변경
     */
    public void markAsRead() {
        this.isRead = ReadStatus.READ;
    }

    /**
     * 알림이 읽혔는지 확인
     */
    public boolean isReadStatus() {
        return this.isRead == ReadStatus.READ;
    }
}