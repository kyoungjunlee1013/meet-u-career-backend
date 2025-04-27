package com.highfive.meetu.domain.notification.common.controller;

import com.highfive.meetu.domain.notification.common.dto.NotificationMessage;
import com.highfive.meetu.domain.notification.common.dto.NotificationTestRequest;
import com.highfive.meetu.domain.notification.common.entity.Notification;
import com.highfive.meetu.domain.notification.common.service.NotificationService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationWebSocketController {
    private final NotificationService notificationService;

    /**
     * 알림 발송 테스트 (DB 저장 + WebSocket 전송)
     */
    @PostMapping("/send")
    @ResponseBody
    public ResultData<String> sendTestNotification(@RequestBody NotificationTestRequest request) {
        notificationService.createNotification(
            request.getAccountId(),
            request.getMessage(),
            request.getNotificationType() != null ? request.getNotificationType() : Notification.NotificationType.APPLICATION_STATUS,
            request.getRelatedId() != null ? request.getRelatedId() : 0L
        );
        return ResultData.success(1, "테스트 알림 전송 완료");
    }
}
