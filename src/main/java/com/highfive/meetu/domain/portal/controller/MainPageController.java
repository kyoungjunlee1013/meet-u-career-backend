package com.highfive.meetu.domain.portal.controller;

import com.highfive.meetu.domain.portal.dto.MainPageResponseDTO;
import com.highfive.meetu.domain.portal.service.MainPageService;
import com.highfive.meetu.global.common.response.ResultData;
import com.highfive.meetu.infra.oauth.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResultData<MainPageResponseDTO> getMainPage(@RequestParam(defaultValue = "0") int page) {
        return ResultData.success(1, mainPageService.getMainContent(securityUtil.getOptionalAccountId(), page));
    }
}
