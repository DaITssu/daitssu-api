package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.CalendarType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(schema = "course")
class Calendar(
    @Enumerated(EnumType.STRING)
    val type: CalendarType,
    val course: String,
    val dueAt: LocalDateTime,
    val name: String
) : BaseEntity()
