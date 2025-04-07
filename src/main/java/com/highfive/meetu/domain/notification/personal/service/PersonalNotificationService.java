package com.highfive.meetu.domain.notification.personal.service;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.dto.NotificationDTO;
import com.highfive.meetu.domain.notification.common.entity.Notification;
import com.highfive.meetu.domain.notification.common.repository.NotificationRepository;
import com.highfive.meetu.domain.notification.personal.dto.NotificationPageResponseDTO;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalNotificationService {
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public ResultData<NotificationPageResponseDTO> getNotifications(Long accountId, int page, int size) {
        Page<Notification> notifications = notificationRepository.findAllPersonalAndCommon(accountId, PageRequest.of(page, size));
        return ResultData.success(1, NotificationPageResponseDTO.from(notifications));
    }

    @Transactional
    public ResultData<?> markAsRead(Long notificationId, Long userId) {
        Notification noti = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotFoundException("알림이 존재하지 않습니다."));

        // 다른 사용자의 알림이면 예외 처리
        if (!noti.getAccount().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        noti.setIsRead(Notification.ReadStatus.READ);
        return ResultData.success(1, "읽음 처리 완료");
    }

    public ResultData<String> createPersonalNotification(NotificationCreateRequestDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId())
            .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

        Notification notification = Notification.builder()
            .account(account)
            .notificationType(dto.getNotificationType())
            .message(dto.getMessage())
            .relatedId(dto.getRelatedId())
            .isRead(Notification.ReadStatus.UNREAD)
            .build();

        notificationRepository.save(notification);
        return ResultData.success(1, "개인회원 알림이 생성되었습니다.");
    }
}
