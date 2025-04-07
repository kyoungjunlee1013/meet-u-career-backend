package com.highfive.meetu.domain.user.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 계정 엔티티
 *
 * 연관관계:
 * - Account(N) : Company(1) - Account가 주인, @JoinColumn 사용 (기업회원의 소속 회사 정보)
 * - Account(1) : Profile(1) - Account가 비주인, mappedBy 사용 (개인회원의 상세 프로필)
 */
@Entity(name = "account")
@Table(
        indexes = {
                @Index(name = "idx_account_userId", columnList = "userId"),
                @Index(name = "idx_account_email", columnList = "email"),
                @Index(name = "idx_account_oauthId", columnList = "oauthId"),
                @Index(name = "idx_account_accountType", columnList = "accountType"),
                @Index(name = "idx_account_status", columnList = "status")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"company", "profile"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account extends BaseEntity {

    @Column(length = 50, unique = true)
    private String userId;  // 로그인용 ID (일반 로그인 사용자만 사용, OAuth 사용자는 NULL)

    @Column(length = 255)
    private String email;  // 이메일 (OAuth 또는 일반 사용자, 중복 가능)

    @Column(length = 255)
    private String password;  // 비밀번호 (일반 로그인 시 사용, OAuth 로그인 시 NULL)

    @Column(length = 50, nullable = false)
    private String name;  // 이름

    @Column(length = 20, nullable = false)
    private String phone;  // 연락처

    @Column(nullable = false)
    private LocalDate birthday;  // 생년월일 (필수값, YYYY-MM-DD 형식)

    @Column(nullable = false)
    private Integer accountType;  // 회원 계정 유형 (PERSONAL, BUSINESS)

    @Column(length = 100)
    private String position;  // 기업 계정인 경우 담당 직책 (예: HR Manager, CTO 등)

    @Column(nullable = true)
    private Integer oauthProvider;  // OAuth 제공자 (GOOGLE, KAKAO, NAVER 등)

    @Column(length = 255, unique = true)
    private String oauthId;  // OAuth 사용자 고유 ID (OAuth 로그인 시 사용)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId")
    private Company company;  // 기업 계정의 경우 소속된 회사 (FK)

    @Column(length = 500)
    private String businessFileUrl;  // 사업자등록증 이미지 파일의 URL 또는 경로

    @Column(length = 255)
    private String businessFileName;  // 업로드된 파일의 원본 이름

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 계정 정보 수정일

    @Column(nullable = false)
    private Integer status;  // 계정 상태 (ACTIVE, INACTIVE, PENDING_APPROVAL, REJECTED)

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"account"})
    private Profile profile;  // 구직자 프로필 (개인 계정만 해당)

    /**
     * 계정 유형
     */
    public static class AccountType {
        public static final int PERSONAL = 0;
        public static final int BUSINESS = 1;
    }

    /**
     * OAuth 제공자 코드
     */
    public static class OAuthProvider {
        public static final int GOOGLE = 1;
        public static final int KAKAO = 2;
        public static final int NAVER = 3;
    }

    /**
     * 계정 상태
     */
    public static class Status {
        public static final int ACTIVE = 0;
        public static final int INACTIVE = 1;
        public static final int PENDING_APPROVAL = 2;
        public static final int REJECTED = 3;
    }
}