package com.highfive.meetu.domain.application.personal.controller;

import com.highfive.meetu.domain.application.personal.dto.InterviewReviewCreateDTO;
import com.highfive.meetu.domain.application.personal.dto.InterviewReviewPersonalDTO;
import com.highfive.meetu.domain.application.personal.service.InterviewReviewPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 개인 회원 면접 후기 작성 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/interview-reviews")
public class InterviewReviewPersonalController {

  private final InterviewReviewPersonalService interviewReviewPersonalService;

  /**
   * 면접 후기 등록 (임시 profileId 고정 버전)
   * JWT 없이 테스트할 수 있도록 임시로 profileId = 1L 고정
   *
   * @param dto 작성용 DTO
   * @return 생성된 후기 ID
   */
  @PostMapping("/create")
  public ResultData<Long> createInterviewReview(@RequestBody InterviewReviewCreateDTO dto) {

    // 임시 고정된 profileId
    Long profileId = 1L;

    // 후기 저장 처리
    Long id = interviewReviewPersonalService.createInterviewReview(dto, profileId);

    // 생성된 ID 반환 (count = 1)
    return ResultData.success(1, id);
  }


  /**
   * 특정 프로필이 작성한 면접 후기 목록 조회
   * 예: GET /api/personal/interview-reviews?profileId=1
   */
  @GetMapping("/list")
  public ResultData<List<InterviewReviewPersonalDTO>> findAllByProfileId() {
    Long profileId = 1L;
    List<InterviewReviewPersonalDTO> result = interviewReviewPersonalService.findAllByProfileId(profileId);
    return ResultData.success(result.size(), result);
  }
}
