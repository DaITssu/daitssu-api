package com.example.domain.main.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
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
    @JsonIgnore
    @JoinColumn(name = "course_id")
    var course : Course? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
}