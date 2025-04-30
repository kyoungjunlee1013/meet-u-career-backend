package com.highfive.meetu.domain.application.business.service;

import com.highfive.meetu.domain.application.business.dto.ApplicationDetailDTO;
import com.highfive.meetu.domain.application.business.dto.ResumeApplicationDetailDTO;
import com.highfive.meetu.domain.application.common.entity.Application;
import com.highfive.meetu.domain.application.common.repository.ApplicationRepository;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final S3Service s3Service;

    public List<ApplicationDetailDTO> getApplications() {
        return applicationRepository.findApplicationDetails();
    }

    public ResumeApplicationDetailDTO getResumeDetailByApplicationId(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new NotFoundException("지원 정보를 찾을 수 없습니다."));

        Resume resume = application.getResume();
        String presignedUrl = resume.getResumeFileKey() != null
            ? s3Service.generatePresignedUrl(resume.getResumeFileKey())
            : null;

        return ResumeApplicationDetailDTO.from(application, presignedUrl);
    }
}
