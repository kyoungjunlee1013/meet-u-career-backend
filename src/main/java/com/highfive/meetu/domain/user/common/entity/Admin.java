package com.highfive.meetu.domain.user.common.entity;

import com.highfive.meetu.domain.user.common.type.AdminTypes.Role;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 관리자 엔티티
 */
@Entity(name = "admin")
@Table(
        indexes = {
                @Index(name = "idx_admin_email", columnList = "email"),
                @Index(name = "idx_admin_role", columnList = "role")
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin extends BaseEntity {

    @Column(length = 255, nullable = false, unique = true)
    private String email;  // 관리자 이메일 (로그인 ID)

    @Column(length = 255, nullable = false)
    private String password;  // 비밀번호 (해시 저장)

    @Column(length = 50, nullable = false)
    private String name;  // 관리자 이름

    @Column(nullable = false)
    private Role role;  // 관리자 역할 (SUPER, ADMIN) - 컨버터 자동 적용

    /**
     * 관리자가 슈퍼 관리자인지 확인
     */
    public boolean isSuperAdmin() {
        return this.role == Role.SUPER;
    }
}