package com.example.test.domain.course.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "calendar")
class Calendar (
    @Column(name = "type", nullable = false)
    val type: CalendarCourseType,
    
    @Column(name = "course", nullable = false)
    val course: String,
    
    @Column(name = "due_at", nullable = false)
    val dueAt: LocalDateTime,
    
    @Column(name = "name", nullable = false)
    val name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id", nullable = false)
    val id: Long? = null
}