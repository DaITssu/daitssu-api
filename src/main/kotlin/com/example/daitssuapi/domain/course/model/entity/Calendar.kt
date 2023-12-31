package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

@Entity
class Calendar(
    @Enumerated(EnumType.STRING)
    var type: CalendarType,
    var course: String,
    var dueAt: LocalDateTime,
    var name: String,
    var isCompleted: Boolean,
    val userId: Long? = null
) : BaseEntity() {
    fun updateCalendar(calendarRequest: CalendarRequest, dueAt: LocalDateTime) {
        this.type = calendarRequest.type
        this.course = calendarRequest.course
        this.name = calendarRequest.name
        this.dueAt = dueAt
        this.isCompleted = calendarRequest.isCompleted
    }
}
