package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Calendar(
    @Enumerated(EnumType.STRING)
    var type: CalendarType,
    @ManyToOne(fetch = FetchType.LAZY)
    var course: Course,
    var dueAt: LocalDateTime,
    var name: String,
    var isCompleted: Boolean,
    val userId: Long? = null
) : BaseEntity() {
    fun updateCalendar(calendarRequest: CalendarRequest, dueAt: LocalDateTime, course: Course) {
        this.type = calendarRequest.type
        this.course = course
        this.name = calendarRequest.name
        this.dueAt = dueAt
        this.isCompleted = calendarRequest.isCompleted
    }
}
