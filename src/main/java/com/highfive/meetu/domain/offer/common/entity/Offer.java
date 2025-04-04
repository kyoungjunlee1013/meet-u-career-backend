package com.highfive.meetu.domain.offer.common.entity;

import com.highfive.meetu.domain.chat.common.entity.ChatRoom;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 채용 제안 엔티티
 *
 * 연관관계:
 * - Account(1) : Offer(N) - Offer가 주인, @JoinColumn 사용 (businessAccount)
 * - Company(1) : Offer(N) - Offer가 주인, @JoinColumn 사용
 * - Account(1) : Offer(N) - Offer가 주인, @JoinColumn 사용 (personalAccount)
 * - ChatRoom(1) : Offer(1) - Offer가 주인, @JoinColumn 사용
 */
@Entity(name = "offer")
@Table(
        indexes = {
                @Index(name = "idx_offer_businessAccountId", columnList = "businessAccountId"),
                @Index(name = "idx_offer_personalAccountId", columnList = "personalAccountId"),
                @Index(name = "idx_offer_companyId", columnList = "companyId"),
                @Index(name = "idx_offer_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"businessAccount", "company", "personalAccount", "chatRoom"})
public class Offer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessAccountId", nullable = false)
    private Account businessAccount;  // 제안을 보낸 채용 담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;  // 제안을 보낸 담당자가 소속된 기업

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personalAccountId", nullable = false)
    private Account personalAccount;  // 제안을 받은 구직자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false, unique = true)
    private ChatRoom chatRoom;  // 채팅방 (한 채팅방당 하나의 제안만 가능)

    @Column(nullable = false)
    private Integer status;  // 제안 상태 (PENDING, ACCEPTED, REJECTED, EXPIRED)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;  // 제안 메시지

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 상태 변경일

    /**
     * 제안 상태값
     */
    public static class Status {
        public static final int PENDING = 0;    // 대기 중
        public static final int ACCEPTED = 1;   // 수락됨
        public static final int REJECTED = 2;   // 거절됨
        public static final int EXPIRED = 3;    // 만료됨
    }
}