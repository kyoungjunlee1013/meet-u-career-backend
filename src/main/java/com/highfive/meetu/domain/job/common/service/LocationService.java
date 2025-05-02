package com.highfive.meetu.domain.job.common.service;

import com.highfive.meetu.domain.job.common.dto.LocationOptionDTO;
import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public ResultData<List<LocationOptionDTO>> getProvinceDropdown() {
        List<LocationOptionDTO> rawList = locationRepository.findProvincesForDropdown();

        // 사용자 친화적 표기 변경 + 확장된 필드에 값 추가
        List<LocationOptionDTO> refined = rawList.stream()
                .map(item -> new LocationOptionDTO(
                        item.getId(),
                        convertProvinceName(item.getLabel()), // label: 사용자에게 보여줄 이름
                        item.getLabel(),                     // province: 원본값 그대로
                        null                                  // city: 1단계에서는 필요 없음
                ))
                .collect(Collectors.toList());

        return ResultData.success(refined.size(), refined);
    }


    /**
     * 2단계 지역 드롭다운 (시/군/구)
     * //@param province 선택된 도/특별시/광역시
     * @return 해당 지역에 속한 시/군/구 목록
     */
    @Transactional(readOnly = true)
    public ResultData<List<LocationOptionDTO>> getCitiesByProvince(String province) {
        List<LocationOptionDTO> rawList = locationRepository.findCitiesByProvince(province);

        List<LocationOptionDTO> refined = rawList.stream()
                .map(item -> new LocationOptionDTO(
                        item.getId(),
                        item.getLabel(),    // label: 시/군/구
                        province,           // province: 요청에서 받은 값
                        item.getLabel()     // city: label = 시/군/구 그대로 사용

                ))
                .toList();


        System.out.println(refined.size());
        return ResultData.success(refined.size(), refined);
    }





    private String convertProvinceName(String original) {
        return switch (original) {
            case "제주특별자치도" -> "제주도";
            case "세종특별자치시" -> "세종시";
            default -> original;
        };
    }

}
