package com.highfive.meetu.domain.coverletter.personal.service;

import com.highfive.meetu.domain.coverletter.common.entity.CoverLetter;
import com.highfive.meetu.domain.coverletter.common.repository.CoverLetterRepository;
import com.highfive.meetu.domain.coverletter.personal.dto.CoverLetterPersonalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoverLetterPersonalService {

    private final CoverLetterRepository coverLetterRepository;

    public List<CoverLetterPersonalDTO> getCoverLetterList(Long profileId) {
        List<CoverLetter> entities = coverLetterRepository.findByProfileIdAndStatusOrderByUpdatedAtDesc(
                profileId, CoverLetter.Status.ACTIVE
        );

        return entities.stream()
                .map(CoverLetterPersonalDTO::fromEntity)
                .toList();
    }

}
