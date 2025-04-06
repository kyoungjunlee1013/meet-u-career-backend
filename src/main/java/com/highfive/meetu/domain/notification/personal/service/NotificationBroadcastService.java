package com.highfive.meetu.domain.notification.personal.service;

import com.highfive.meetu.domain.notification.personal.dto.NotificationCreateRequestDTO;
import com.highfive.meetu.domain.notification.common.entity.Notification;
import com.highfive.meetu.domain.notification.common.repository.NotificationRepository;
import com.highfive.meetu.domain.user.common.repository.AccountRepository;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationBroadcastService {
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    public ResultData<String> createCommonNotification(NotificationCreateRequestDTO dto) {
        List<Account> allUsers = accountRepository.findAll();

        for (Account account : allUsers) {
            Notification notification = Notification.builder()
                .account(account)
                .notificationType(dto.getNotificationType())
                .message(dto.getMessage())
                .relatedId(dto.getRelatedId())
                .isRead(Notification.ReadStatus.UNREAD)
                .build();
            notificationRepository.save(notification);
        }

        return ResultData.success(1, "공통 알림이 전체 회원에게 전송되었습니다.");
    }
}
