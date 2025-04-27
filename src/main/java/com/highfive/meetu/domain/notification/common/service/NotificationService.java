package com.highfive.meetu.domain.notification.common.service;

import com.highfive.meetu.domain.notification.common.dto.NotificationMessage;
import com.highfive.meetu.domain.notification.common.entity.Notification;
import com.highfive.meetu.domain.notification.common.repository.NotificationRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 알림 목록 조회
     * - 안읽은 알림 전체 + 읽은 알림 최근 7일 이내
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationList() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 안읽은 알림 전체 가져오기
        List<Notification> unreadNotifications = notificationRepository.findByAccountIdAndIsReadOrderByCreatedAtDesc(
            SecurityUtil.getAccountId(), Notification.ReadStatus.UNREAD
        );

        // 읽은 알림 중 7일 이내만 가져오기
        List<Notification> readNotifications = notificationRepository.findByAccountIdAndIsReadAndCreatedAtAfterOrderByCreatedAtDesc(
            SecurityUtil.getAccountId(), Notification.ReadStatus.READ, sevenDaysAgo
        );

        // 합치기 (안읽은 → 읽은 순)
        List<Notification> combined = new ArrayList<>();
        combined.addAll(unreadNotifications);
        combined.addAll(readNotifications);

        return combined;
    }

    /**
     * 새로운(읽지 않은) 알림이 있는지 여부 조회
     */
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications() {
        System.out.println("------------ accountId (2) : " + SecurityUtil.getAccountId());

        return notificationRepository.findAllByAccountIdAndIsRead(SecurityUtil.getAccountId(), Notification.ReadStatus.UNREAD);
    }

    /**
     * 단일 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        // 본인 알림인지 검증
        if (!notification.getAccount().getId().equals(SecurityUtil.getAccountId())) {
            throw new IllegalStateException("본인의 알림만 읽음 처리할 수 있습니다.");
        }

        // 읽음 표시
        notification.setIsRead(Notification.ReadStatus.READ);
    }

    /**
     * 특정 사용자의 모든 알림 읽음 처리
     */
    @Transactional
    public int markAllAsRead() {
        return notificationRepository.markAllAsReadByAccountId(SecurityUtil.getAccountId());
    }

    /**
     * 알림 생성 및 WebSocket 발송
     */
    @Transactional
    public void createNotification(Long accountId, String message, int notificationType, Long relatedId) {
        // 1. Account 조회
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 2. Notification 저장
        Notification notification = Notification.builder()
            .account(account)
            .notificationType(notificationType)
            .message(message)
            .relatedId(relatedId)
            .isRead(Notification.ReadStatus.UNREAD)
            .build();
        notificationRepository.save(notification);

        // 3. WebSocket 푸시 발송
        NotificationMessage notificationMessage = new NotificationMessage(
            accountId,
            message,
            notification.getCreatedAt().toString(),
            notification.getNotificationType()
        );

        messagingTemplate.convertAndSend("/topic/notification/" + accountId, notificationMessage);
    }
}
