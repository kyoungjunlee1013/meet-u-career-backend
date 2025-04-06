package com.highfive.meetu.domain.notification.personal.dto;

import com.highfive.meetu.domain.notification.common.entity.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationDTO {
    private Long id;
    private String message;
    private int notificationType;
    private Long relatedId;
    private int isRead;
    private LocalDateTime createdAt;

    public static NotificationDTO from(Notification notification) {
        return NotificationDTO.builder()
            .id(notification.getId())
            .message(notification.getMessage())
            .notificationType(notification.getNotificationType())
            .relatedId(notification.getRelatedId())
            .isRead(notification.getIsRead())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
