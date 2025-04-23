package com.highfive.meetu.domain.application.business.controller;

import com.highfive.meetu.domain.application.business.dto.ApplicationDetailDTO;
import com.highfive.meetu.domain.application.business.dto.ResumeApplicationDetailDTO;
import com.highfive.meetu.domain.application.business.service.ApplicationService;
import com.highfive.meetu.domain.resume.common.entity.Resume;
import com.highfive.meetu.domain.resume.common.repository.ResumeRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ResumeRepository resumeRepository;
    private final S3Service fileStorageService;

    @GetMapping("/details")
    public ResponseEntity<List<ApplicationDetailDTO>> getApplicationDetails() {
        return ResponseEntity.ok(applicationService.getApplications());
    }

    @GetMapping("/resume/detail/{applicationId}")
    public ResultData<ResumeApplicationDetailDTO> getResumeDetail(@PathVariable Long applicationId) {
        ResumeApplicationDetailDTO dto = applicationService.getResumeDetailByApplicationId(applicationId);
        return ResultData.success(1, dto);
    }

    @GetMapping("/resume/download/{resumeId}")
    public ResponseEntity<Resource> downloadResumeFile(@PathVariable Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
            .orElseThrow(() -> new NotFoundException("이력서를 찾을 수 없습니다."));

        if (resume.getResumeFileKey() == null) {
            throw new IllegalArgumentException("첨부파일이 존재하지 않습니다.");
        }

        String fileKey = resume.getResumeFileKey();
        Resource fileResource = fileStorageService.loadAsResource(fileKey);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getResumeFileName() + "\"")
            .body(fileResource);
    }
}
