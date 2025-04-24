package com.highfive.meetu.domain.community.admin.service;

import com.highfive.meetu.domain.community.admin.dto.CommunityTagDTO;
import com.highfive.meetu.domain.community.admin.dto.TagSearchRequestDTO;
import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import com.highfive.meetu.domain.community.common.repository.CommunityTagRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityTagAdminService {

    private final CommunityTagRepository communityTagRepository;

    // 태그 생성
    public Long createTag(String name) {
        CommunityTag tag = CommunityTag.builder()
            .name(name)
            .status(CommunityTag.Status.ACTIVE)
            .build();
        communityTagRepository.save(tag);
        return tag.getId();
    }

    // 전체 또는 상태 필터 조회
    public Page<CommunityTagDTO> getTags(TagSearchRequestDTO request) {
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, request.getSortBy()));

        Page<CommunityTag> page = (request.getStatus() == null)
            ? communityTagRepository.findAll(pageable)
            : communityTagRepository.findAllByStatus(request.getStatus(), pageable);

        return page.map(CommunityTagDTO::from);
    }

    // 상태 토글 (활성 ↔ 비활성)
    public void toggleStatus(Long tagId) {
        CommunityTag tag = communityTagRepository.findById(tagId)
            .orElseThrow(() -> new NotFoundException("태그를 찾을 수 없습니다."));
        tag.setStatus(tag.getStatus() == CommunityTag.Status.ACTIVE
            ? CommunityTag.Status.INACTIVE
            : CommunityTag.Status.ACTIVE);
    }
}
