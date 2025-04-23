package com.highfive.meetu.domain.dashboard.admin.controller;

import com.highfive.meetu.domain.dashboard.admin.dto.AdminDashboardResponseDTO;
import com.highfive.meetu.domain.dashboard.admin.service.AdminDashboardService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {
    private final AdminDashboardService dashboardService;

    @GetMapping("/metrics")
    public ResultData<AdminDashboardResponseDTO> getMetrics() {
        return ResultData.success(1, dashboardService.getMetrics());
    }
}