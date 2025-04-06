package com.highfive.meetu.domain.notification.common.repository;

import com.highfive.meetu.domain.notification.common.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByAccountId(Long accountId, Pageable pageable);
}
