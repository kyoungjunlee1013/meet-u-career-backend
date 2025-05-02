package com.highfive.meetu.domain.job.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationOptionDTO {

    private Long id;           // location 테이블의 ID
    private String label;      // 프론트에 보여줄 이름 (ex. "서울특별시" 또는 "강남구")
    private String province;   // 1단계 지역 (서울특별시, 경기도 등)
    private String city;       // 2단계 지역 (성남시 분당구, 강남구 등)

    // QueryDSL 전용 생성자
    public LocationOptionDTO(Long id, String label) {
        this.id = id;
        this.label = label;
    }

}
