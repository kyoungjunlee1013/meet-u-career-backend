package com.highfive.meetu.domain.notification.common.repository;

import com.highfive.meetu.domain.notification.common.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 개인회원(accountId 일치) 또는 공통 알림(notificationType = 21 또는 22)
    @Query("""
        SELECT n FROM notification n
        WHERE n.account.id = :accountId
           OR n.notificationType IN (:#{T(com.highfive.meetu.domain.notification.common.entity.Notification.NotificationType).SYSTEM_NOTIFICATION},
                                     :#{T(com.highfive.meetu.domain.notification.common.entity.Notification.NotificationType).ACCOUNT_SECURITY})
        ORDER BY n.createdAt DESC
    """)
    Page<Notification> findAllPersonalAndCommon(@Param("accountId") Long accountId, Pageable pageable);
}
