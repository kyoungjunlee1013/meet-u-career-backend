package com.highfive.meetu.domain.notification.personal.service;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.personal.dto.NotificationDTO;
import com.highfive.meetu.domain.notification.common.entity.Notification;
import com.highfive.meetu.domain.notification.common.repository.NotificationRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalNotificationService {
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public ResultData<Page<NotificationDTO>> getNotifications(Long userId, int page, int size) {
        Page<NotificationDTO> notifications = notificationRepository.findAllByAccountId(userId, PageRequest.of(page, size))
            .map(NotificationDTO::from);

        return ResultData.success(1, notifications);
    }

    @Transactional
    public ResultData<?> markAsRead(Long notificationId) {
        return notificationRepository.findById(notificationId)
            .map(noti -> {
                noti.setIsRead(Notification.ReadStatus.READ);
                return ResultData.success(1, "읽음 처리 완료");
            }).orElse(ResultData.fail("알림이 존재하지 않습니다."));
    }

    public ResultData<String> createPersonalNotification(NotificationCreateRequestDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
        if (account == null) {
            return ResultData.fail("회원이 존재하지 않습니다.");
        }

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
