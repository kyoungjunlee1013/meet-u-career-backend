package com.highfive.meetu.domain.job.admin.controller;

import com.highfive.meetu.domain.job.admin.dto.JobPostingAdminDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.admin.service.JobPostingAdminService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/job-postings")
public class JobPostingAdminController {

  private final JobPostingAdminService jobPostingAdminService;

  @GetMapping
  public ResultData<?> getAllJobPostings(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) Integer status
  ) {
    PageRequest pageRequest = PageRequest.of(page, 30);
    Page<JobPostingAdminDTO> resultPage = jobPostingAdminService.findAllByStatus(pageRequest, status);
    return ResultData.success(resultPage.getContent().size(), resultPage.getContent());
  }

  @PatchMapping("/{id}/approve")
  public ResultData<?> approveJobPosting(@PathVariable Long id) {
    jobPostingAdminService.approve(id);
    return ResultData.success(1, null);
  }


  @PatchMapping("/{id}/reject")
  public ResultData<?> rejectJobPosting(@PathVariable Long id) {
    jobPostingAdminService.reject(id);
    return ResultData.success(1, null);
  }

  @PatchMapping("/{id}/block")
  public ResultData<?> blockJobPosting(@PathVariable Long id) {
    jobPostingAdminService.block(id);
    return ResultData.success(1, null);
  }

    // DB에 저장된 채용 공고 조회
    @GetMapping
    public List<JobPosting> getAllJobPostings() {
        return jobPostingAdminService.getAllJobPostings();
    }

    // 사람인 API를 호출 후 채용공고 업데이트 (DB 저장)
    @PostMapping("/fetch")
    public String updateJobPostings() {
        jobPostingAdminService.updateJobPostings();  // 변경된 함수 호출
        return "저장 완료!";
    }

    // 금융위 기업정보 API 테스트 (회사명으로 호출)
    @GetMapping("/company")
    public String testFinanceApi(@RequestParam String name, String industry) {
        jobPostingAdminService.callCompanyInfo(name, industry);  // 변경된 함수 호출
        return "API 호출 완료!";
    }
}
