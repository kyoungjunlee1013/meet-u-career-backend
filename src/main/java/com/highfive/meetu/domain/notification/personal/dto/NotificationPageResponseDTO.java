package com.highfive.meetu.domain.notification.personal.dto;

import com.highfive.meetu.domain.notification.common.entity.Notification;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPageResponseDTO {
    private int currentPage;
    private int totalPages;
    private int totalElements;
    private List<NotificationDTO> notifications;

    public static NotificationPageResponseDTO from(Page<Notification> page) {
        return NotificationPageResponseDTO.builder()
            .currentPage(page.getNumber())
            .totalPages(page.getTotalPages())
            .totalElements((int) page.getTotalElements())
            .notifications(page.getContent().stream()
                .map(NotificationDTO::from)
                .collect(Collectors.toList()))
            .build();
    }
}
