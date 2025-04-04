package com.highfive.meetu.domain.calendar.common.repository;

import com.highfive.meetu.domain.calendar.common.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

}
