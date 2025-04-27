package com.highfive.meetu.domain.notification.common.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private Long accountId;
    private String message;
    private String createdAt;
    private Integer notificationType;
}
