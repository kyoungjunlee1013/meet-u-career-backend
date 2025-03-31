package com.highfive.meetu.domain.chat.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.chat.common.type.ChatRoomTypes.Status;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 * 채팅방 엔티티
 *
 * 연관관계:
 * - Company(1) : ChatRoom(N) - ChatRoom이 주인, @JoinColumn 사용
 * - Account(1) : ChatRoom(N) - ChatRoom이 주인, @JoinColumn 사용 (businessAccount)
 * - Account(1) : ChatRoom(N) - ChatRoom이 주인, @JoinColumn 사용 (personalAccount)
 * - JobPosting(1) : ChatRoom(N) - ChatRoom이 주인, @JoinColumn 사용
 * - ChatRoom(1) : ChatMessage(N) - ChatRoom이 비주인, mappedBy 사용
 */
@Entity(name = "chatRoom")
@Table(
        indexes = {
                @Index(name = "idx_chatroom_businessAccountId", columnList = "businessAccountId"),
                @Index(name = "idx_chatroom_personalAccountId", columnList = "personalAccountId"),
                @Index(name = "idx_chatroom_companyId", columnList = "companyId"),
                @Index(name = "idx_chatroom_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"company", "businessAccount", "personalAccount", "jobPosting", "messages"})
public class ChatRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;  // 채팅을 개설한 기업

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessAccountId", nullable = false)
    private Account businessAccount;  // 채팅을 시작한 채용 담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personalAccountId", nullable = false)
    private Account personalAccount;  // 채팅을 받은 구직자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobPostingId")
    private JobPosting jobPosting;  // 관련된 채용 공고 (NULL 가능)

    @Column(nullable = false)
    private Status status;  // 채팅방 상태 (OPEN, CLOSED) - 컨버터 자동 적용

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "chatRoom",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"chatRoom"})
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    /**
     * 채팅 메시지를 추가하는 편의 메서드
     */
    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        message.setChatRoom(this);
    }

    /**
     * 채팅방 상태를 업데이트하는 메서드
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * 채팅방이 열려있는지 확인
     */
    public boolean isOpen() {
        return this.status == Status.OPEN;
    }
}