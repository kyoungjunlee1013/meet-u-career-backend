package com.highfive.meetu.domain.community.admin.dto;

import com.highfive.meetu.domain.community.common.entity.CommunityTag;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommunityTagDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer status;

    public static CommunityTagDTO from(CommunityTag tag) {
        return new CommunityTagDTO(tag.getId(), tag.getName(), tag.getCreatedAt(), tag.getUpdatedAt(), tag.getStatus());
    }
}