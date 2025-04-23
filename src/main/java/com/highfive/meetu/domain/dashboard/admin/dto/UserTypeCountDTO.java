package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UserTypeCountDTO {
    private String type; // personal, business, admin
    private long count;
}
