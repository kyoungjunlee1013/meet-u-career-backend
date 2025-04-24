package com.highfive.meetu.domain.calendar.personal.controller;

import com.highfive.meetu.domain.calendar.personal.dto.CalendarPersonalDTO;
import com.highfive.meetu.domain.calendar.personal.dto.PublicCalendarItemDTO;
import com.highfive.meetu.domain.calendar.personal.service.CalendarPersonalService;
import com.highfive.meetu.global.common.response.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal/calendar")
public class CalendarPersonalController {

    private final CalendarPersonalService calendarPersonalService;


    /**
     * [비로그인용] 마감 임박 공고 일정 리스트
     */
    @GetMapping("/list")
    public ResultData<List<PublicCalendarItemDTO>> getPublicScheduleList() {
        List<PublicCalendarItemDTO> list = calendarPersonalService.getPublicScheduleList();
        return ResultData.success(list.size(), list);
    }



    @GetMapping("/list")
    public ResultData<List<CalendarPersonalDTO>> getFullCalendarForMe() {
        //Long accountId = SecurityUtil.getAccountId(); // 인증된 사용자 ID
        Long accountId = 1L;
        List<CalendarPersonalDTO> schedules = calendarPersonalService.getFullScheduleForAccount(accountId);
        return ResultData.success(schedules.size(), schedules);
    }




    /**
     * [1] 회원(개인회원, 기업회원) 일정 전체 조회
     */
//    @GetMapping("/list")
//    public ResultData<List<CalendarPersonalDTO>> getPersonalCalendarList() {
//        //Long accountId = SecurityUtil.getAccountId(); // 인증된 사용자 ID
//        Long accountId = 1L;
//        List<CalendarPersonalDTO> list = calendarPersonalService.getScheduleList(accountId);
//        return ResultData.success(list.size(), list);
//    }

    /**
     * [2] 일정 등록
     */
    @PostMapping("/create")
    public ResultData<Long> createCalendar(@RequestBody CalendarPersonalDTO dto) {

        //Long accountId = SecurityUtil.getAccountId(); // 인증된 사용자 ID
        Long accountId = 1L;

        Long id = calendarPersonalService.addSchedule(accountId, dto);
        return ResultData.success(1, id);
    }

    /**
     * [3] 일정 상세 조회
     */
    @GetMapping("/detail/{calendarEventId}")
    public ResultData<CalendarPersonalDTO> getCalendarDetail(@PathVariable Long calendarEventId) {
        CalendarPersonalDTO dto = calendarPersonalService.getScheduleDetail(calendarEventId);
        return ResultData.success(1, dto);
    }

    /**
     * [4] 일정 수정
     */
    @PostMapping("/update/{calendarEventId}")
    public ResultData<String> updateCalendar(@PathVariable Long calendarEventId, @RequestBody CalendarPersonalDTO dto) {
        dto.setId(calendarEventId); // calendarId를 dto에 세팅
        calendarPersonalService.updateSchedule(dto);
        return ResultData.success(1, "일정이 수정되었습니다.");
    }

    /**
     * [5] 일정 삭제
     */
    @PostMapping("/delete/{calendarEventId}")
    public ResultData<String> deleteCalendar(@PathVariable Long calendarEventId) {
        calendarPersonalService.deleteSchedule(calendarEventId);
        return ResultData.success(1, "일정이 삭제되었습니다.");
    }
}

