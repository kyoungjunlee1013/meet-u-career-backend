package com.highfive.meetu.domain.chat.common.entity;

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
    private Integer senderType;  // 보낸 사람 유형 (PERSONAL, BUSINESS)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;  // 채팅 메시지 내용

    @Column(nullable = false)
    private Integer isRead;  // 읽음 여부

    // 상수 정의
    public static class SenderType {
        public static final int PERSONAL = 0;  // 개인 사용자
        public static final int BUSINESS = 1;  // 기업 사용자
    }

    public static class ReadStatus {
        public static final int UNREAD = 0;  // 안 읽음
        public static final int READ = 1;    // 읽음
    }
}