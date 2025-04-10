package com.highfive.meetu.domain.calendar.personal.dto;

import com.highfive.meetu.domain.calendar.common.entity.CalendarEvent;
import com.highfive.meetu.domain.user.common.entity.Profile;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarPersonalDTO {

    private Long id; // 일정 ID (수정, 삭제, 상세조회 시 필요)

    private Long profileId; // FK: 개인 회원의 프로필 ID

    private String title; // 일정 제목 (필수)

    private String description; // 일정 설명

    private LocalDate date; // 날짜 (연도-월-일)

    private LocalTime startTime; // 시작 시간

    private LocalTime endTime; // 종료 시간

    private Integer status; // 상태값 (1: 활성, 0: 삭제)

    private Integer type;
    // 일정 유형
    // 1: 개인일정 (내가 직접 추가)
    // 2: 지원 일정 (공고 지원 시 자동 추가)
    // 3: 마감 일정 (관심 공고 마감일 자동 추가)

    public static CalendarPersonalDTO fromEntity(com.highfive.meetu.domain.calendar.common.entity.CalendarEvent entity) {
        return CalendarPersonalDTO.builder()
                .id(entity.getId())
                .profileId(entity.getAccount().getId()) // accountId → profileId로 표현
                .title(entity.getTitle())
                .description(entity.getDescription())
                .date(entity.getStartDateTime().toLocalDate()) // LocalDateTime → LocalDate
                .startTime(entity.getStartDateTime().toLocalTime())
                .endTime(entity.getEndDateTime().toLocalTime())
                .type(entity.getEventType()) // 일정 유형
                .build(); // status는 없으므로 제외
    }

    public CalendarEvent toEntity(Profile profile) {
        return CalendarEvent.builder()
                .account(profile.getAccount()) // ✅ 해결!
                .title(this.title)
                .description(this.description)
                .eventType(this.type != null ? this.type : CalendarEvent.EventType.PERSONAL_EVENT)
                .startDateTime(LocalDateTime.of(this.date, this.startTime != null ? this.startTime : LocalTime.MIN))
                .endDateTime(LocalDateTime.of(this.date, this.endTime != null ? this.endTime : LocalTime.MAX))
                .isAllDay(this.startTime == null && this.endTime == null)
                .build();
    }



}
