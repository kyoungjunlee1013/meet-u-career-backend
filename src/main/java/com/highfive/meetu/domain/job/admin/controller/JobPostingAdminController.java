package com.highfive.meetu.domain.job.admin.controller;

import com.highfive.meetu.domain.job.admin.dto.JobPostingAdminDTO;
import com.highfive.meetu.domain.job.admin.service.JobPostingAdminService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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
}
