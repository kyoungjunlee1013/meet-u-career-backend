package com.highfive.meetu.domain.job.business.controller;

import com.highfive.meetu.domain.job.business.service.ResumeDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 이력서 다운로드 컨트롤러
 * - 사전 동의 여부 확인 후 이력서를 PDF로 다운로드
 */
@RestController
@RequestMapping("/api/business/resume")
@RequiredArgsConstructor
public class ResumeDownloadController {

    private final ResumeDownloadService resumeDownloadService;

    @GetMapping("/{resumeId}/download")
    public void downloadResume(
        @PathVariable Long resumeId,
        @RequestParam(name = "agreed") boolean agreed,
        HttpServletResponse response
    ) throws IOException {
        if (!agreed) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "사용자 동의가 필요합니다.");
            return;
        }

        resumeDownloadService.downloadResumeAsPdf(resumeId, response);
    }
}
