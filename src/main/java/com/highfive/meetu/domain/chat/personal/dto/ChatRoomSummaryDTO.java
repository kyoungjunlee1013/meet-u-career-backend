package com.highfive.meetu.domain.chat.personal.dto;

import com.highfive.meetu.domain.chat.common.entity.ChatMessage;
import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomSummaryDTO {
    private Long roomId;
    private String name;              // 상대방 이름
    private String avatar;            // 상대방 프로필 이미지
    private String lastMessage;
    private String lastMessageTime;   // 포맷된 시간 (ex: 5분 전)
    private int unreadCount;

    public static ChatRoomSummaryDTO of(ChatRoom room, Account me, ChatMessage lastMessage, int unreadCount, String avatar) {
        Account opponent = room.getBusinessAccount().equals(me)
                ? room.getPersonalAccount()
                : room.getBusinessAccount();

        return ChatRoomSummaryDTO.builder()
                .roomId(room.getId())
                .name(opponent.getName())
                .avatar(avatar)
                .lastMessage(lastMessage != null ? lastMessage.getMessage() : "")
                .lastMessageTime(lastMessage != null ? TimeUtils.formatTimeAgo(lastMessage.getCreatedAt()) : "")
                .unreadCount(unreadCount)
                .build();
    }
}