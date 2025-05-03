package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.admin.dto.JobPostingAdminDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobPostingAdminRepository {

  Page<JobPostingAdminDTO> findAllByStatus(Pageable pageable, Integer status);

  void approve(Long id);

  void reject(Long id);

  void block(Long id);
}
