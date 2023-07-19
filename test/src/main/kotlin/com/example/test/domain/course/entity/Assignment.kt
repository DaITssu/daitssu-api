package com.example.test.domain.course.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "assignment")
class Assignment (
    @Column(name = "due_at", nullable = false)
    val dueAt : LocalDateTime,

    @Column(name = "start_at", nullable = false)
    val startAt : LocalDateTime,
    
    @Column(name = "name", nullable = false)
    val name: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course : Course? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
    
    
}