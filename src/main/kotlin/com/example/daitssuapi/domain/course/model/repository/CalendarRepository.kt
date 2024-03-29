package com.example.daitssuapi.domain.course.model.repository

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.domain.course.model.entity.Calendar
import com.example.daitssuapi.domain.course.model.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CalendarRepository : JpaRepository<Calendar, Long> {
    fun findByUserIdAndDueAtBetween(
        userId:Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): List<Calendar>
    
    fun findDistinctTop2ByUserIdAndTypeAndDueAtBetweenOrderByDueAtAsc(
        userId: Long,
        type: CalendarType,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ) : List<Calendar>
    
    fun findByUserIdAndTypeAndCourseAndDueAtBetween(
        userId: Long,
        type: CalendarType,
        course: Course,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ) : List<Calendar>
}