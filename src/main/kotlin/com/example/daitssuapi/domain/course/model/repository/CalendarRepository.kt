package com.example.daitssuapi.domain.course.model.repository

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.domain.course.dto.response.TodayCalendarMapping
import com.example.daitssuapi.domain.course.model.entity.Calendar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CalendarRepository : JpaRepository<Calendar, Long> {
    fun findByDueAtBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<Calendar>
    
    
    @Query(value =
    "SELECT c.course as course , MIN(c.dueAt) as dueAt, COUNT(*) as count " +
        "FROM Calendar c " +
        "WHERE c.course IN ( " +
            "SELECT c2.course " +
            "FROM Calendar c2 " +
            "WHERE (c2.course, c2.dueAt) IN ( " +
                "SELECT DISTINCT c3.course, c3.dueAt " +
                "FROM Calendar c3 " +
                "WHERE c3.type = :type " +
                "AND c3.dueAt BETWEEN :startDateTime AND :endDateTime " +
                "ORDER BY c3.dueAt " +
                "LIMIT 2 " +
            ") AND c.dueAt BETWEEN :startDateTime AND :endDateTime " +
        ") " +
        "GROUP BY c.course"
    )
    fun findCourses(
        @Param("startDateTime") startDateTime: LocalDateTime,
        @Param("endDateTime") endDateTime: LocalDateTime,
        @Param("type") type: CalendarType
    ): List<TodayCalendarMapping>
    
    
}