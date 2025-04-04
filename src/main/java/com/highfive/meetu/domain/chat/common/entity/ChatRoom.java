package com.highfive.meetu.domain.chat.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.resume.common.entity.Resume;
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
 * - Resume(1) : ChatRoom(N) - ChatRoom이 주인, @JoinColumn 사용
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
@ToString(exclude = {"company", "businessAccount", "personalAccount", "resume", "messages"})
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
    @JoinColumn(name = "resumeId")
    private Resume resume;  // 연관 이력서 (선택)


    @Column(nullable = false)
    private Integer status;  // 채팅방 상태 (OPEN, CLOSED)

    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "chatRoom",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"chatRoom"})
    @Builder.Default
    private List<ChatMessage> messageList = new ArrayList<>();

    // 상태 코드 정의
    public static class Status {
        public static final int OPEN = 0;    // 채팅방 열림
        public static final int CLOSED = 1;  // 채팅방 닫힘
    }
}