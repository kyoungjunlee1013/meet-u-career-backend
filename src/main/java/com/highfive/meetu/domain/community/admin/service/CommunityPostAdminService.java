package com.highfive.meetu.domain.community.admin.service;

import com.highfive.meetu.domain.community.admin.dto.CommunityPostDTO;
import com.highfive.meetu.domain.community.admin.dto.CommunityPostSearchRequestDTO;
import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import com.highfive.meetu.domain.community.common.repository.CommunityPostRepository;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.highfive.meetu.global.common.response.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommunityPostAdminService {

    private final CommunityPostRepository postRepository;

    /**
     * 게시글 목록 조회 (상태 필터링 + 페이징)
     */
    public Page<CommunityPostDTO> searchPosts(CommunityPostSearchRequestDTO request) {
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Sort sort = Sort.by(direction, request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<CommunityPost> page = (request.getStatus() == null)
            ? postRepository.findAll(pageable)
            : postRepository.findAllByStatus(request.getStatus(), pageable);

        return page.map(CommunityPostDTO::from);
    }

    /**
     * 게시글 상세 조회
     */
    public CommunityPostDTO getPostDetail(Long postId) {
        CommunityPost post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND.getMessage()));

        return CommunityPostDTO.from(post);
    }

    /**
     * 게시글 상태 토글 (0 <-> 1)
     */
    public void togglePostStatus(Long postId) {
        CommunityPost post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND.getMessage()));

        if (post.getStatus() == CommunityPost.Status.ACTIVE) {
            post.setStatus(CommunityPost.Status.DELETED);
        } else {
            post.setStatus(CommunityPost.Status.ACTIVE);
        }

        postRepository.save(post);
    }
}
