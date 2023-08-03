package com.example.domain.main.model.entity

import com.example.domain.main.model.entity.Course
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "video")
class Video (
    
    @Column(name = "due_at", nullable = false)
    var dueAt: LocalDateTime,
    @Column(name = "start_at", nullable = false)
    var startAt: LocalDateTime,
    
    @Column(name = "name", nullable = false)
    var name: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course? = null
    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}