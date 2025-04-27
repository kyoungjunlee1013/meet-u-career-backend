package com.highfive.meetu.domain.dashboard.admin.dto;

public class AgeGroupDTO {
    private String ageGroup;
    private Long count;

    public AgeGroupDTO(String ageGroup, Long count) {
        this.ageGroup = ageGroup;
        this.count = count;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public Long getCount() {
        return count;
    }
}

