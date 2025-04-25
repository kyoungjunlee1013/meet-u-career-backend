package com.highfive.meetu.domain.user.business.dto;

import com.highfive.meetu.domain.company.common.entity.Company;
import com.highfive.meetu.domain.user.common.entity.Account;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessSignUpRequestDTO {

  // Account 관련
  private String userId;
  private String email;
  private String password;
  private String phone;
  private String name;
  private LocalDate birthday;
  private String position; // 기업 담당자 직책
  private String businessFileKey;
  private String businessFileName;

  // Company 관련
  private String businessNumber;
  private String companyName;
  private String address;
  private String industry;
  private LocalDate foundedDate;
  private Integer numEmployees;
  private Long revenue;
  private String website;
  private String logoKey;

  // 약관 동의
  private Boolean serviceConsent;
  private Boolean privacyConsent;

  // 상태
  private Integer status;

  // Company 엔티티로 변환
  public Company toCompany() {
    return Company.builder()
        .businessNumber(businessNumber)
        .name(companyName)
        .address(address)
        .industry(industry)
        .foundedDate(foundedDate)
        .numEmployees(numEmployees != null ? numEmployees : 0)
        .revenue(revenue != null ? revenue : 0L)
        .website(website)
        .logoKey(logoKey)
        .status(status != null ? status : Status.WAITING)
        .build();
  }

  // Account 엔티티로 변환
  public Account toAccount(String encodedPassword, Company company) {
    return Account.builder()
        .userId(userId)
        .email(email)
        .password(encodedPassword)
        .name(name)
        .phone(phone)
        .birthday(birthday)
        .position(position)
        .accountType(Account.AccountType.BUSINESS)
        .status(status != null ? status : Status.WAITING)
        .businessFileKey(businessFileKey)
        .businessFileName(businessFileName)
        .company(company)
        .build();
  }

    // Entity → DTO 변환
    public static BusinessSignUpRequestDTO from(Account account, Company company) {
        return BusinessSignUpRequestDTO.builder()
            .email(account.getEmail())
            .phone(account.getPhone())
            .name(account.getName())
            .birthday(account.getBirthday())
            .businessNumber(company.getBusinessNumber())
            .companyName(company.getName())
            .address(company.getAddress())
            .industry(company.getIndustry())
            .foundedDate(company.getFoundedDate())
            .numEmployees(company.getNumEmployees())
            .revenue(company.getRevenue())
            .website(company.getWebsite())
            .logoKey(company.getLogoKey())
            .status(account.getStatus())
            .build();
    }

    /**
     * 상태값
     */
    public static class Status {
        public static final int ACTIVE = 0;  // 활성 상태
        public static final int INACTIVE = 1;   // 비활성
        public static final int WAITING = 2; // 기업계정 승인 대기 중
        public static final int REJECTED = 3; // 기업계정 반려됨
    }
}
