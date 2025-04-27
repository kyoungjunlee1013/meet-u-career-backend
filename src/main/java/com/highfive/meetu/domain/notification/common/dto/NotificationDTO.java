package com.highfive.meetu.domain.notification.common.dto;

import com.highfive.meetu.domain.notification.common.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 조회용 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;               // 알림 ID
    private Integer notificationType; // 알림 유형
    private String message;        // 알림 내용
    private Long relatedId;        // 관련된 데이터 ID
    private Integer isRead;        // 읽음 여부 (0/1)
    private String createdAt;      // 생성일 (String 포맷)

    /**
     * Entity -> DTO 변환 메서드
     */
    public static NotificationDTO fromEntity(Notification notification) {
        return NotificationDTO.builder()
            .id(notification.getId())
            .notificationType(notification.getNotificationType())
            .message(notification.getMessage())
            .relatedId(notification.getRelatedId())
            .isRead(notification.getIsRead())
            .createdAt(notification.getCreatedAt().toString()) // 필요 시 포맷팅 따로 가능
            .build();
    }
}