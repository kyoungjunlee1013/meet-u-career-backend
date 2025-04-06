package com.highfive.meetu.domain.notification.personal.controller;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.service.NotificationBroadcastService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/common")
public class NotificationBroadcastController {
    private final NotificationBroadcastService notificationBroadcastService;

    @PostMapping
    public ResultData<String> broadcastNotification(@RequestBody NotificationCreateRequestDTO dto) {
        return notificationBroadcastService.createCommonNotification(dto);
    }
}
