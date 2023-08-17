package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.CalendarType
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(schema = "course")
class Calendar(
    val type: CalendarType,
    val course: String,
    val dueAt: LocalDateTime,
    val name: String
) : BaseEntity()
