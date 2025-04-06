package com.highfive.meetu.domain.notification.personal.controller;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.dto.NotificationDTO;
import com.highfive.meetu.domain.notification.personal.service.PersonalNotificationService;
import com.highfive.meetu.global.common.response.ResultData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/personal")
public class PersonalNotificationController {
    private final PersonalNotificationService personalNotificationService;

    @GetMapping
    public ResultData<Page<NotificationDTO>> getNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        return personalNotificationService.getNotifications(userId, page, size);
    }

    @PostMapping("/{notificationId}/read")
    public ResultData<?> markAsRead(@PathVariable Long notificationId) {
        return personalNotificationService.markAsRead(notificationId);
    }

    @PostMapping("/create")
    public ResultData<String> createNotification(@RequestBody NotificationCreateRequestDTO dto) {
        return personalNotificationService.createPersonalNotification(dto);
    }
}
