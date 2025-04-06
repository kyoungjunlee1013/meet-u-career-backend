package com.highfive.meetu.domain.notification.personal.dto;

import lombok.*;

@Getter
@Setter
public class NotificationCreateRequestDTO {
    private Long accountId;
    private Integer notificationType;
    private String message;
    private Long relatedId;
}
