package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import com.example.domain.main.model.entity.Course
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class Video (

    var dueAt: LocalDateTime,
    var startAt: LocalDateTime,
    var name: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    var course: Course? = null
    
) : BaseEntity()