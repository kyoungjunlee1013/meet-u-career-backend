package com.highfive.meetu.domain.notification.personal.controller;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.dto.NotificationPageResponseDTO;
import com.highfive.meetu.domain.notification.personal.service.PersonalNotificationService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/personal")
public class PersonalNotificationController {
    private final PersonalNotificationService personalNotificationService;
    private final HttpServletRequest request;

    @GetMapping
    public ResultData<NotificationPageResponseDTO> getNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = RequestUtil.getAccountId(request);
        return personalNotificationService.getNotifications(userId, page, size);
    }

    @PostMapping("/{notificationId}/read")
    public ResultData<?> markAsRead(@PathVariable Long notificationId) {
        Long userId = RequestUtil.getAccountId(request);
        return personalNotificationService.markAsRead(notificationId, userId);
    }

    @PostMapping("/create")
    public ResultData<String> createNotification(@RequestBody NotificationCreateRequestDTO dto) {
        return personalNotificationService.createPersonalNotification(dto);
    }
}
