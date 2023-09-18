package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(schema = "course")
class Video(
    var dueAt: LocalDateTime,

    var startAt: LocalDateTime,

    var name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course? = null
) : BaseEntity()
