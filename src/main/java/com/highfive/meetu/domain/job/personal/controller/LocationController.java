package com.highfive.meetu.domain.job.personal.controller;

import com.highfive.meetu.domain.job.common.repository.LocationRepository;
import com.highfive.meetu.domain.job.personal.dto.LocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

// LocationController.java
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
    private final LocationRepository locationRepository;

    @GetMapping("/locations")
    public List<LocationDTO> allLocations() {
        return locationRepository.findAllByOrderByIdAsc()
                .stream()
                .map(LocationDTO::fromEntity)        // Entity → DTO 변환
                .collect(Collectors.toList());       // Stream → List
    }

}

