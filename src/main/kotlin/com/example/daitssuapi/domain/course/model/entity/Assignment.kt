package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Assignment(
    val name: String,

    val dueAt: LocalDateTime? = null,

    val startAt: LocalDateTime? = null,

    val submitAt: LocalDateTime? = null,

    val detail: String? = null,

    val comments: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course,
) : BaseEntity()
