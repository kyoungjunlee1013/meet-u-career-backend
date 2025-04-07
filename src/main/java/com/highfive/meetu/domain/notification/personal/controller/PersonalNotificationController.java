package com.highfive.meetu.domain.notification.personal.controller;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.dto.NotificationPageResponseDTO;
import com.highfive.meetu.domain.notification.personal.service.PersonalNotificationService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.global.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.highfive.meetu.global.util.RequestUtil.getUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/personal")
public class PersonalNotificationController {
    private final PersonalNotificationService personalNotificationService;

    @GetMapping
    public ResultData<NotificationPageResponseDTO> getNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        return personalNotificationService.getNotifications(userId, page, size);
    }

    @PostMapping("/{notificationId}/read")
    public ResultData<?> markAsRead(
        @PathVariable Long notificationId,
        HttpServletRequest request
    ) {
        Long userId = RequestUtil.getUserId(request); // 공통 유틸 사용
        return personalNotificationService.markAsRead(notificationId, userId);
    }

    @PostMapping("/create")
    public ResultData<String> createNotification(@RequestBody NotificationCreateRequestDTO dto) {
        return personalNotificationService.createPersonalNotification(dto);
    }
}
