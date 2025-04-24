package com.highfive.meetu.domain.dashboard.business.controller;

import com.highfive.meetu.domain.dashboard.business.dto.BusinessDashboardDTO;
import com.highfive.meetu.domain.dashboard.business.dto.CompanyProfileDTO;
import com.highfive.meetu.domain.dashboard.business.service.BusinessDashboardService;
import com.highfive.meetu.domain.user.common.entity.Account;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/dashboard")
@RequiredArgsConstructor
@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true" // ✅ 쿠키 포함 허용
)
public class BusinessDashboardController {

  private final BusinessDashboardService businessDashboardService;

  /**
   * 기업 대시보드 정보 조회 (companyId 기반)
   */
  @GetMapping("/{companyId}")
  public ResultData<BusinessDashboardDTO> getDashboard(@PathVariable Long companyId) {
    BusinessDashboardDTO dto = businessDashboardService.getDashboard(companyId);
    return ResultData.success(1, dto);
  }

  /**
   * 로그인한 기업 사용자 기준 대시보드
   */
  @GetMapping("/me")
  public ResultData<BusinessDashboardDTO> getDashboardForLoggedInCompany(
      @AuthenticationPrincipal Account account) {
    BusinessDashboardDTO dto = businessDashboardService.getDashboardByAccount(account);
    return ResultData.success(1, dto);
  }

  /**
   * 로그인된 기업 사용자 기준 프로필 정보 반환
   */
  @GetMapping("/profile/me")
  public ResultData<CompanyProfileDTO> getMyCompanyProfile(
      @AuthenticationPrincipal Account account) {
    CompanyProfileDTO dto = businessDashboardService.getCompanyProfile(account);
    return ResultData.success(1, dto);
  }
}