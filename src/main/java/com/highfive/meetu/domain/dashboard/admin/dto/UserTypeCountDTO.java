package com.highfive.meetu.domain.dashboard.admin.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserTypeCountDTO {
    private Integer accountType;
    private Long count;
}
