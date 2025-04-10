package com.highfive.meetu.domain.calendar.personal.service;

import com.highfive.meetu.domain.calendar.common.entity.CalendarEvent;
import com.highfive.meetu.domain.calendar.common.repository.CalendarEventRepository;
import com.highfive.meetu.domain.calendar.personal.dto.CalendarPersonalDTO;
import com.highfive.meetu.domain.user.common.entity.Profile;
import com.highfive.meetu.domain.user.common.repository.ProfileRepository;
import com.highfive.meetu.global.common.exception.BadRequestException;
import com.highfive.meetu.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarPersonalService {

    private final CalendarEventRepository calendarEventRepository;
    private final ProfileRepository profileRepository;

    /**
     * [2] 일정 등록
     */
    @Transactional
    public Long addSchedule(CalendarPersonalDTO dto) {
        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new NotFoundException("프로필이 존재하지 않습니다."));

        CalendarEvent schedule = dto.toEntity(profile);
        calendarEventRepository.save(schedule);
        return schedule.getId();
    }

    /**
     * [1] 개인 일정 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CalendarPersonalDTO> getScheduleList(Long accountId) {
        List<CalendarEvent> list = calendarEventRepository.findAllByAccount_Id(accountId);
        return list.stream().map(CalendarPersonalDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * [3] 일정 상세 조회
     */
    @Transactional(readOnly = true)
    public CalendarPersonalDTO getScheduleDetail(Long id) {
        CalendarEvent entity = calendarEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("일정을 찾을 수 없습니다."));
        return CalendarPersonalDTO.fromEntity(entity);
    }

    /**
     * [4] 일정 수정
     */
    @Transactional
    public void updateSchedule(CalendarPersonalDTO dto) {
        CalendarEvent entity = calendarEventRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("수정할 일정을 찾을 수 없습니다."));

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setEventType(dto.getType()); // 일정 유형

        // 날짜 및 시간 → LocalDateTime으로 조립
        LocalDate date = dto.getDate();
        LocalTime startTime = dto.getStartTime() != null ? dto.getStartTime() : LocalTime.MIN;
        LocalTime endTime = dto.getEndTime() != null ? dto.getEndTime() : LocalTime.MAX;

        entity.setStartDateTime(LocalDateTime.of(date, startTime));
        entity.setEndDateTime(LocalDateTime.of(date, endTime));
        entity.setIsAllDay(dto.getStartTime() == null && dto.getEndTime() == null); // 종일 여부
    }


    /**
     * [5] 일정 삭제
     */
    @Transactional
    public void deleteSchedule(Long id) {
        CalendarEvent entity = calendarEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("삭제할 일정을 찾을 수 없습니다."));

        // 삭제 가능한지 체크: relatedId가 null인 경우만 삭제 허용
        if (entity.getRelatedId() != null) {
            throw new BadRequestException("해당 일정은 시스템 연동 일정으로 삭제할 수 없습니다.");
        }

        calendarEventRepository.delete(entity);
    }

    // 특정 날짜에 일정 있는지 확인
    @Transactional(readOnly = true)
    public boolean hasScheduleOnDate(Long accountId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return calendarEventRepository.existsByAccount_IdAndStartDateTimeBetween(accountId, start, end);
    }

}
