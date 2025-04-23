package com.highfive.meetu.domain.application.personal.controller;

import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewReviewPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
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
  public ResultData<List<InterviewReviewPersonalDTO>> getList(@RequestParam Long profileId) {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findAllByProfileId(profileId);
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

  @GetMapping("/company/info/{companyId}")
  public ResultData<InterviewCompanySummaryDTO> getCompanySummary(@PathVariable Long companyId) {
    InterviewCompanySummaryDTO dto = interviewReviewPersonalService.getCompanySummary(companyId);
    return ResultData.success(1,dto);
  }

  @GetMapping("/reviews/recently")
  public ResultData<List<InterviewReviewPersonalDTO>> getHotRecentReviews() {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.getTop10RecentReviews();
    return ResultData.success(list.size(), list);
  }
  @GetMapping("/company/{companyId}")
  public ResultData<List<InterviewReviewPersonalDTO>> getReviewsByCompany(@PathVariable Long companyId) {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findByCompanyId(companyId);
    return ResultData.success(list.size(), list);
  }
}