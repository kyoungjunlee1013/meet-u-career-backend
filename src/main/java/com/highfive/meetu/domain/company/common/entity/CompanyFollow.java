package com.highfive.meetu.domain.company.common.entity;

import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 관심 기업 엔티티
 *
 * 연관관계:
 * - Account(1) : CompanyFollow(N) - CompanyFollow가 주인, @JoinColumn 사용
 * - Company(1) : CompanyFollow(N) - CompanyFollow가 주인, @JoinColumn 사용
 */
@Entity(name = "companyFollow")
@Table(
        indexes = {
                @Index(name = "idx_company_follow_accountId", columnList = "accountId"),
                @Index(name = "idx_company_follow_companyId", columnList = "companyId")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_account_company", columnNames = {"accountId", "companyId"})
        }
)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"account", "company"})
public class CompanyFollow extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;  // 팔로우한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;  // 팔로우한 기업
}