package com.highfive.meetu.domain.dashboard.admin.dto;

public class ConversionRateDTO {
    private String label;
    private double rate;

    public ConversionRateDTO(String label, double rate) {
        this.label = label;
        this.rate = rate;
    }

    public String getLabel() {
        return label;
    }

    public double getRate() {
        return rate;
    }
}
