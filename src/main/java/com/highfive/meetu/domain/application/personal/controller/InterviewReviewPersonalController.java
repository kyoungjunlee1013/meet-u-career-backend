package com.highfive.meetu.domain.application.personal.controller;

import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewReviewPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/interview-reviews")
@RequiredArgsConstructor
@CrossOrigin(
    origins = "http://localhost:3000",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST},
    allowCredentials = "true"
)
public class InterviewReviewPersonalController {

  private final InterviewReviewPersonalService interviewReviewPersonalService;

  @GetMapping
  public ResultData<List<InterviewReviewPersonalDTO>> getList() {
    Long AccountId = SecurityUtil.getAccountId();
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findAllByProfileId(AccountId);
    return ResultData.success(list.size(), list);
  }

  @GetMapping("/companies")
  public ResultData<List<InterviewCompanySummaryDTO>> getCompaniesWithReviews() {
    List<InterviewCompanySummaryDTO> list = interviewReviewPersonalService.getCompaniesWithReview();
    return ResultData.success(list.size(), list);
  }

  @GetMapping("/companies/search")
  public ResultData<List<InterviewCompanySummaryDTO>> searchCompanies(@RequestParam String keyword) {
    List<InterviewCompanySummaryDTO> list = interviewReviewPersonalService.searchCompaniesWithReview(keyword);
    return ResultData.success(list.size(), list);
  }

  @GetMapping("/company/info/")
  public ResultData<InterviewCompanySummaryDTO> getCompanySummary() {
    Long AccountId = SecurityUtil.getAccountId();
    InterviewCompanySummaryDTO dto = interviewReviewPersonalService.getCompanySummary(AccountId);
    return ResultData.success(1,dto);
  }

  @GetMapping("/reviews/recently")
  public ResultData<List<InterviewReviewPersonalDTO>> getHotRecentReviews() {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.getTop10RecentReviews();
    return ResultData.success(list.size(), list);
  }
  @GetMapping("/company")
  public ResultData<List<InterviewReviewPersonalDTO>> getReviewsByCompany() {
    Long AccountId = SecurityUtil.getAccountId();
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findByCompanyId(AccountId);
    return ResultData.success(list.size(), list);
  }
}