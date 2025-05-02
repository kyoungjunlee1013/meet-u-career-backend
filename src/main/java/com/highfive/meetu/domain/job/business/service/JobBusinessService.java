package com.highfive.meetu.domain.job.business.service;

import com.highfive.meetu.domain.job.business.dto.JobPostingBusinessDTO;
import com.highfive.meetu.domain.job.common.entity.JobPosting;
import com.highfive.meetu.domain.job.common.repository.JobPostingRepository;
import com.highfive.meetu.domain.payment.business.dto.AdvertisementDTO;
import com.highfive.meetu.domain.payment.common.entity.Advertisement;
import com.highfive.meetu.domain.payment.common.repository.AdvertisementRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobBusinessService {

    private final JobPostingRepository jobPostingRepository;
    private final AdvertisementRepository advertisementRepository;

    // 특정 기업의 전체 공고 목록 조회
    public ResultData<List<JobPostingBusinessDTO>> getJobPostingsByCompany(Long companyId) {
        List<JobPosting> postings = jobPostingRepository.findByCompanyId(companyId);
        List<JobPostingBusinessDTO> dtoList = new ArrayList<>();
        
        // 현재 시간 기준
        LocalDateTime now = LocalDateTime.now();
        
        // 현재 진행 중이거나 미래에 예정된 광고만 가져오기
        List<Advertisement> relevantAds = advertisementRepository.findCurrentOrFutureAdsByCompanyId(
            companyId, Advertisement.Status.ACTIVE, now);
        
        // 공고ID를 키로 하는 광고 맵 생성 (리스트 형태로 저장)
        Map<Long, List<Advertisement>> adsMap = new HashMap<>();
        for (Advertisement ad : relevantAds) {
            Long jobPostingId = ad.getJobPosting().getId();
            adsMap.computeIfAbsent(jobPostingId, k -> new ArrayList<>()).add(ad);
        }
        
        // 단일 광고 정보용 맵 (기존 호환성)
        Map<Long, Advertisement> firstAdMap = relevantAds.stream()
            .collect(Collectors.toMap(
                ad -> ad.getJobPosting().getId(),
                ad -> ad,
                // 여러 개가 있다면 시작일이 빠른 것 선택
                (ad1, ad2) -> ad1.getStartDate().isBefore(ad2.getStartDate()) ? ad1 : ad2
            ));
    
        for (JobPosting posting : postings) {
            List<Long> categoryIds = posting.getJobPostingJobCategoryList().stream()
                    .map(jc -> jc.getJobCategory().getId())
                    .toList();
    
            JobPostingBusinessDTO dto = JobPostingBusinessDTO.fromEntity(posting, categoryIds);
            
            // 해당 공고의 광고 리스트 설정
            List<Advertisement> jobAds = adsMap.getOrDefault(posting.getId(), Collections.emptyList());
            List<AdvertisementDTO> adDtos = jobAds.stream()
                .map(ad -> AdvertisementDTO.fromEntity(ad, now))
                .toList();
            dto.setAdvertisements(adDtos);
            
            // 기존 호환성을 위한 단일 광고 정보 설정
            Advertisement ad = firstAdMap.get(posting.getId());
            if (ad != null) {
                boolean isCurrentlyActive = ad.getStartDate().isBefore(now) && 
                                            ad.getEndDate().isAfter(now);
                
                dto.setAdType(ad.getAdType());
                dto.setAdTypeLabel(convertAdTypeToLabel(ad.getAdType()));
                dto.setAdStartDate(ad.getStartDate());
                dto.setAdEndDate(ad.getEndDate());
                dto.setIsAdvertised(isCurrentlyActive); // 현재 활성화된 경우만 true
            }
            
            dtoList.add(dto);
        }

        return ResultData.success(dtoList.size(), dtoList);
    }

    // 공고 상세 조회 (결제 등에서 사용)
    public ResultData<JobPostingBusinessDTO> getJobPostingById(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("공고를 찾을 수 없습니다."));

        // 상세도 목록과 동일하게 categoryIds 추출
        List<Long> categoryIds = jobPosting.getJobPostingJobCategoryList().stream()
                .map(jc -> jc.getJobCategory().getId())
                .toList();

        JobPostingBusinessDTO dto = JobPostingBusinessDTO.fromEntity(jobPosting, categoryIds);
        
        // 현재 시간 기준
        LocalDateTime now = LocalDateTime.now();
        
        // 현재 활성화된 또는 미래 예정 광고 조회
        List<Advertisement> ads = advertisementRepository.findCurrentOrFutureAdsByJobPostingId(id, Advertisement.Status.ACTIVE, now);
        
        // 광고 정보를 DTO로 변환하여 리스트에 설정
        List<AdvertisementDTO> adDtos = ads.stream()
            .map(ad -> AdvertisementDTO.fromEntity(ad, now))
            .toList();
        
        dto.setAdvertisements(adDtos);
        
        // 기존 단일 광고 필드도 호환성을 위해 유지 (가장 먼저 시작하는 광고 선택)
        if (!ads.isEmpty()) {
            // 시작일이 가장 빠른 광고 선택
            Advertisement ad = ads.stream()
                .min((a1, a2) -> a1.getStartDate().compareTo(a2.getStartDate()))
                .orElse(null);
            
            if (ad != null) {
                boolean isCurrentlyActive = ad.getStartDate().isBefore(now) && 
                                            ad.getEndDate().isAfter(now);
                
                dto.setAdType(ad.getAdType());
                dto.setAdTypeLabel(convertAdTypeToLabel(ad.getAdType()));
                dto.setAdStartDate(ad.getStartDate());
                dto.setAdEndDate(ad.getEndDate());
                dto.setIsAdvertised(isCurrentlyActive); // 현재 활성화된 경우만 true
            }
        }
        
        return ResultData.success(1, dto);
    }
    
    // 광고 타입 라벨 변환
    private String convertAdTypeToLabel(Integer adType) {
        return switch (adType) {
            case Advertisement.AdType.BASIC -> "기본";
            case Advertisement.AdType.STANDARD -> "스탠다드";
            case Advertisement.AdType.PREMIUM -> "프리미엄";
            default -> "미지정";
        };
    }
}
