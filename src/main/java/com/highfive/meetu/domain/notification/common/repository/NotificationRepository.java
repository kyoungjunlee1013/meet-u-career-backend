package com.highfive.meetu.domain.notification.common.repository;

import com.highfive.meetu.domain.notification.common.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * 안읽은 알림 전체 조회 (최신순)
     */
    List<Notification> findByAccountIdAndIsReadOrderByCreatedAtDesc(Long accountId, Integer isRead);

    /**
     * 읽은 알림 중 최근 7일 이내 조회 (최신순)
     */
    List<Notification> findByAccountIdAndIsReadAndCreatedAtAfterOrderByCreatedAtDesc(Long accountId, Integer isRead, LocalDateTime createdAt);

    /**
     * 계정 ID 기준 읽지 않은 알림 전체 조회
     *
     * @param accountId 계정 ID
     * @param isRead 읽음 여부 (0: 읽지 않음)
     * @return 읽지 않은 알림 리스트
     */
    List<Notification> findAllByAccountIdAndIsRead(Long accountId, Integer isRead);

    /**
     * 계정 ID 기준 모든 알림 읽음 처리
     *
     * @param accountId 계정 ID
     */
    @Modifying
    @Query("UPDATE notification n SET n.isRead = 1 WHERE n.account.id = :accountId")
    int markAllAsReadByAccountId(@Param("accountId") Long accountId);
}
