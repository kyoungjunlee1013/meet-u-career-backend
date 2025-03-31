package com.highfive.meetu.global.common.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*
    모든 엔티티 클래스에서 상속받을 수 있는 공통 기본 엔티티.
    기본적으로 id와 createdAt 필드를 제공하여, 각 엔티티의 고유 식별자와 생성일자를 관리.
 */
@Getter
@Setter
@SuperBuilder  // Lombok의 SuperBuilder를 사용하여, 빌더 패턴으로 객체 생성 가능
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자를 보호된 접근 수준으로 생성 (상속을 위한 기본 생성자)
@MappedSuperclass  // 이 클래스는 실제 테이블로 매핑되지 않으며, 상속받은 클래스에서 매핑됨
@EntityListeners(AuditingEntityListener.class)  // AuditingEntityListener를 사용하여 생성일자 자동 관리
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "createdAt", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
