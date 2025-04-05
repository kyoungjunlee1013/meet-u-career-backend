package com.highfive.meetu.domain.portal.controller;

import com.highfive.meetu.domain.portal.dto.MainPageResponse;
import com.highfive.meetu.domain.portal.service.MainPageService;
import com.highfive.meetu.global.common.response.ResultData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping
    public ResultData<MainPageResponse> getMainPage(
        @RequestParam(defaultValue = "0") int page,
        HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId"); // JWT 필터에서 설정된 userId
        return ResultData.success(1, mainPageService.getMainContent(userId, page));
    }
}
