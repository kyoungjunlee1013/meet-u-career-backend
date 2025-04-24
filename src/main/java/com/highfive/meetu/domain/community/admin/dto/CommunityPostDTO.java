package com.highfive.meetu.domain.community.admin.dto;

import com.highfive.meetu.domain.community.common.entity.CommunityPost;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 커뮤니티 게시글 응답 DTO
 */
@Getter
@Builder
public class CommunityPostDTO {
    private Long id;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private int status;
    private String tagName;
    private String writerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommunityPostDTO from(CommunityPost post) {
        return CommunityPostDTO.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .tagName(post.getTag().getName())
            .writerName(post.getAccount().getName())
            .likeCount(post.getLikeCount())
            .commentCount(post.getCommentCount())
            .status(post.getStatus())
            .build();
    }
}
