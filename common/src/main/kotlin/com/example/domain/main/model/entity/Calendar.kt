package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import com.example.common.enums.CalendarCourseType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Calendar (
    val type: CalendarCourseType,
    
    val course: String,
    
    val dueAt: LocalDateTime,
    
    val name: String
) : BaseEntity()