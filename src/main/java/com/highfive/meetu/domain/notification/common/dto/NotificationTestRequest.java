package com.highfive.meetu.domain.notification.common.dto;

import lombok.Data;

@Data
public class NotificationTestRequest {
    private Long accountId;
    private String message;
    private Integer notificationType;
    private Long relatedId;
}