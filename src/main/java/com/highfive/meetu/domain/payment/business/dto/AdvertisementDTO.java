package com.highfive.meetu.domain.payment.business.dto;

import com.highfive.meetu.domain.payment.common.entity.Advertisement;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDTO {
    private Long id;
    private Integer adType;           // 광고 유형 (1: BASIC, 2: STANDARD, 3: PREMIUM)
    private String adTypeLabel;       // 광고 유형 라벨
    private LocalDateTime startDate;  // 광고 시작일
    private LocalDateTime endDate;    // 광고 종료일
    private Boolean isActive;         // 현재 활성화 여부
    
    public static AdvertisementDTO fromEntity(Advertisement ad, LocalDateTime now) {
        boolean isActive = ad.getStartDate().isBefore(now) && ad.getEndDate().isAfter(now);
        
        return AdvertisementDTO.builder()
                .id(ad.getId())
                .adType(ad.getAdType())
                .adTypeLabel(convertAdTypeToLabel(ad.getAdType()))
                .startDate(ad.getStartDate())
                .endDate(ad.getEndDate())
                .isActive(isActive)
                .build();
    }
    
    private static String convertAdTypeToLabel(Integer adType) {
        return switch (adType) {
            case Advertisement.AdType.BASIC -> "기본";
            case Advertisement.AdType.STANDARD -> "스탠다드";
            case Advertisement.AdType.PREMIUM -> "프리미엄";
            default -> "미지정";
        };
    }
}
