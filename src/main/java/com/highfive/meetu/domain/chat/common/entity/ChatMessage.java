package com.highfive.meetu.domain.chat.common.entity;

import com.highfive.meetu.domain.chat.common.type.ChatMessageTypes.ReadStatus;
import com.highfive.meetu.domain.chat.common.type.ChatMessageTypes.SenderType;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 채팅 메시지 엔티티
 *
 * 연관관계:
 * - ChatRoom(1) : ChatMessage(N) - ChatMessage가 주인, @JoinColumn 사용
 * - Account(1) : ChatMessage(N) - ChatMessage가 주인, @JoinColumn 사용
 */
@Entity(name = "chatMessage")
@Table(
        indexes = {
                @Index(name = "idx_message_chatRoomId", columnList = "chatRoomId"),
                @Index(name = "idx_message_senderId", columnList = "senderId"),
                @Index(name = "idx_message_isRead", columnList = "isRead")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"chatRoom", "sender"})
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false)
    private ChatRoom chatRoom;  // 메시지가 속한 채팅방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private Account sender;  // 메시지를 보낸 사용자

    @Column(nullable = false)
    private SenderType senderType;  // 보낸 사람 유형 (PERSONAL, BUSINESS) - 컨버터 자동 적용

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;  // 채팅 메시지 내용

    @Column(nullable = false)
    private ReadStatus isRead;  // 읽음 여부 - 컨버터 자동 적용

    /**
     * 메시지를 읽음 상태로 변경
     */
    public void markAsRead() {
        this.isRead = ReadStatus.READ;
    }

    /**
     * 메시지가 읽혔는지 확인
     */
    public boolean isReadStatus() {
        return this.isRead == ReadStatus.READ;
    }
}