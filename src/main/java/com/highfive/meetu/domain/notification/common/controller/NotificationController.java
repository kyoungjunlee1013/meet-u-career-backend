package com.highfive.meetu.domain.notification.common.controller;

import com.highfive.meetu.domain.notification.common.dto.NotificationReadRequest;
import com.highfive.meetu.domain.notification.common.entity.Notification;
import com.highfive.meetu.domain.notification.common.service.NotificationService;
import com.highfive.meetu.domain.notification.common.dto.NotificationDTO;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 알림 목록 조회 API
     * - 안읽은 알림 전체 + 읽은 알림 최근 7일 이내
     */
    @GetMapping("/list")
    public ResultData<List<NotificationDTO>> getNotificationList() {
        List<Notification> notifications = notificationService.getNotificationList();
        List<NotificationDTO> result = notifications.stream()
            .map(NotificationDTO::fromEntity)
            .toList();
        return ResultData.success(result.size(), result);
    }

    /**
     * 신규 알림 여부 확인
     */
    @GetMapping("/new")
    public ResultData<List<NotificationDTO>> checkNewNotification() {
        List<Notification> newNotifications = notificationService.getUnreadNotifications();
        List<NotificationDTO> result = newNotifications.stream()
            .map(NotificationDTO::fromEntity)
            .toList();
        return ResultData.success(result.size(), result);
    }

    /**
     * 알림 읽음 처리
     */
    @PostMapping("/read")
    public ResultData<Long> markNotificationAsRead(@RequestBody NotificationReadRequest request) {
        notificationService.markAsRead(request.getNotificationId());
        return ResultData.success(1, request.getNotificationId());
    }

    /**
     * 알림 전체 읽음 처리
     */
    @PostMapping("/readall")
    public ResultData<Void> markAllNotificationsAsRead() {
        int updatedCount = notificationService.markAllAsRead();
        return ResultData.success(updatedCount, null);
    }
}

