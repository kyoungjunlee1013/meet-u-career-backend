package com.highfive.meetu.domain.dashboard.business.controller;

import com.highfive.meetu.domain.dashboard.business.dto.BusinessDashboardDTO;
import com.highfive.meetu.domain.dashboard.business.dto.CompanyProfileDTO;
import com.highfive.meetu.domain.dashboard.business.service.BusinessDashboardService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/dashboard/info")
@RequiredArgsConstructor
@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true"
)
public class BusinessDashboardController {

  private final BusinessDashboardService businessDashboardService;

  @GetMapping
  public ResultData<BusinessDashboardDTO> getDashboard() {
    Long accountId = SecurityUtil.getAccountId();
    BusinessDashboardDTO dto = businessDashboardService.getDashboard(accountId);
    return ResultData.success(1, dto);
  }

  @GetMapping("/profile")
  public ResultData<CompanyProfileDTO> getCompanyProfile() {
    Long accountId = SecurityUtil.getAccountId();
    CompanyProfileDTO dto = businessDashboardService.getCompanyProfile(accountId);
    return ResultData.success(1, dto);
  }

  @PutMapping("/profile")
  public ResultData<Long> updateCompanyProfile(@RequestBody CompanyProfileDTO dto) {
    Long accountId = SecurityUtil.getAccountId();
    Long id = businessDashboardService.updateCompanyProfile(accountId, dto);
    return ResultData.success(1, id);
  }
}
