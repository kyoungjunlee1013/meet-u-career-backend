package com.highfive.meetu.domain.chat.personal.dto;

import com.highfive.meetu.domain.chat.common.entity.ChatMessage;
import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import com.highfive.meetu.domain.user.common.entity.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {

    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private Integer senderType; // 0: 개인, 1: 기업
    private String message;
    private Integer isRead; // 0: 안읽음, 1: 읽음
    private LocalDateTime createdAt;

    public ChatMessage toEntity(ChatRoom chatRoom, Account sender) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .senderType(this.senderType)
                .message(this.message)
                .isRead(ChatMessage.ReadStatus.UNREAD)
                .build();
    }
}
