package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class ApplicationTimeStatDTO {
    private String timeSlot;   // 예: "오전", "1-3시"
    private int applied;       // 지원 완료 수
}