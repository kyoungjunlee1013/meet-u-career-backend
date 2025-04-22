package com.highfive.meetu.domain.job.common.service;

import com.highfive.meetu.domain.job.common.dto.IndustryOptionDTO;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;


}
