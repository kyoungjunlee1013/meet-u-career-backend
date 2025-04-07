package com.highfive.meetu.domain.notification.personal.controller;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.dto.NotificationPageResponseDTO;
import com.highfive.meetu.domain.notification.personal.service.PersonalNotificationService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/personal")
public class PersonalNotificationController {
    private final PersonalNotificationService personalNotificationService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResultData<NotificationPageResponseDTO> getNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Long accountId = securityUtil.getCurrentAccountId();
        return personalNotificationService.getNotifications(accountId, page, size);
    }

    @PostMapping("/{notificationId}/read")
    public ResultData<?> markAsRead(@PathVariable Long notificationId) {
        Long accountId = securityUtil.getCurrentAccountId();
        return personalNotificationService.markAsRead(notificationId, accountId);
    }

    @PostMapping("/create")
    public ResultData<String> createNotification(@RequestBody NotificationCreateRequestDTO dto) {
        return personalNotificationService.createPersonalNotification(dto);
    }
}
