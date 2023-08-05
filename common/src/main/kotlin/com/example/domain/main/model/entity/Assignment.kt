package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Assignment (
    val dueAt : LocalDateTime,
    
    val startAt : LocalDateTime,
    
    val name: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "course_id")
    var course : Course? = null,
) : BaseEntity()
