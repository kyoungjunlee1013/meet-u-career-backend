package com.highfive.meetu.domain.job.admin.service;

import com.highfive.meetu.domain.job.admin.dto.JobPostingAdminDTO;
import com.highfive.meetu.domain.job.common.repository.JobPostingAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobPostingAdminService {

  private final JobPostingAdminRepository jobPostingAdminRepository;

  public Page<JobPostingAdminDTO> findAllByStatus(Pageable pageable, Integer status) {
    return jobPostingAdminRepository.findAllByStatus(pageable, status);
  }

  public void approve(Long id) {
    jobPostingAdminRepository.approve(id);
  }

  public void reject(Long id) {
    jobPostingAdminRepository.reject(id);
  }

  public void block(Long id) {
    jobPostingAdminRepository.block(id);
  }
}
