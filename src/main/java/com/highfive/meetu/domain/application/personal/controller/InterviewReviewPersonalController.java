package com.highfive.meetu.domain.application.personal.controller;

import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.personal.dto.InterviewCompanySummaryDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewReviewPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal/interview-reviews")
@RequiredArgsConstructor
public class InterviewReviewPersonalController {

  private final InterviewReviewPersonalService interviewReviewPersonalService;


    /**
     * (가정) 외부에서 Application 리스트를 주입받는 경우 테스트용
     * 실제로는 queryRepository 또는 다른 팀원의 로직과 연결 필요
     */
    @PostMapping("/reviewable-list")
    public ResultData<List<InterviewReviewApplicationDTO>> getReviewableApplications(
            @RequestBody List<Application> applications) {
        List<InterviewReviewApplicationDTO> result =
                interviewReviewPersonalService.toDTOList(applications);
        return ResultData.success(result.size(), result);
    }

    @GetMapping
    public ResultData<List<InterviewReviewDTO>> getMyReviews() {
        Long profileId = SecurityUtil.getProfileId(); // 현재 로그인한 사용자 기준
        List<InterviewReviewDTO> reviews = interviewReviewPersonalService.getReviewsByProfileId(profileId);
        return ResultData.success(reviews.size(), reviews);

    }

  /**
   * [마이페이지] 내가 작성한 면접 후기 목록 조회
   * - 로그인한 계정 ID를 기준으로 조회
   * - 개인회원용 마이페이지에서 사용
   */
  @GetMapping
  public ResultData<List<InterviewReviewPersonalDTO>> getList() {
    Long AccountId = SecurityUtil.getAccountId();
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findAllByProfileId(AccountId);
    return ResultData.success(list.size(), list);
  }

  /**
   * [공개 목록] 면접 후기가 1개 이상 존재하는 기업 목록 조회
   * - 커뮤니티 혹은 공고 상세에서 사용 가능
   */
  @GetMapping("/companies")
  public ResultData<List<InterviewCompanySummaryDTO>> getCompaniesWithReviews() {
    List<InterviewCompanySummaryDTO> list = interviewReviewPersonalService.getCompaniesWithReview();
    return ResultData.success(list.size(), list);
  }

  /**
   * [검색] 면접 후기가 등록된 기업 중에서 키워드로 기업명 검색
   * - 회사 이름 일부 키워드로 검색
   */
  @GetMapping("/companies/search")
  public ResultData<List<InterviewCompanySummaryDTO>> searchCompanies(@RequestParam String keyword) {
    List<InterviewCompanySummaryDTO> list = interviewReviewPersonalService.searchCompaniesWithReview(keyword);
    return ResultData.success(list.size(), list);
  }

  /**
   * [기업 상세] 특정 회사의 요약 정보 조회
   * - 회사명, 업종, 주소, 홈페이지 등 반환
   * - 프론트에서 회사 후기 페이지 상단에 표시용
   */
  @GetMapping("/company/info/{companyId}")
  public ResultData<InterviewCompanySummaryDTO> getCompanySummary(@PathVariable Long companyId) {
    InterviewCompanySummaryDTO dto = interviewReviewPersonalService.getCompanySummary(companyId);
    return ResultData.success(1, dto);
  }

  /**
   * [최근 인기] 최근 등록된 면접 후기 중 상위 10건 조회
   * - 정렬 기준은 등록일자
   * - 메인 페이지, 추천 탭 등에서 활용 가능
   */
  @GetMapping("/reviews/recently")
  public ResultData<List<InterviewReviewPersonalDTO>> getHotRecentReviews() {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.getTop10RecentReviews();
    return ResultData.success(list.size(), list);
  }

  /**
   * [기업 상세] 특정 기업의 면접 후기 전체 조회
   * - 회사 상세 페이지에서 후기 리스트 렌더링 시 사용
   */
  @GetMapping("/company/{companyId}")
  public ResultData<List<InterviewReviewPersonalDTO>> getReviewsByCompany(@PathVariable Long companyId) {
    List<InterviewReviewPersonalDTO> list = interviewReviewPersonalService.findByCompanyId(companyId);
    return ResultData.success(list.size(), list);
  }

  /**
   * [후기 상세] 특정 후기 ID로 상세 조회
   */
  @GetMapping("/{reviewId}")
  public ResultData<InterviewReviewPersonalDTO> getReviewDetail(@PathVariable Long reviewId) {
    InterviewReviewPersonalDTO dto = interviewReviewPersonalService.findById(reviewId);
    return ResultData.success(1, dto);
  }

}