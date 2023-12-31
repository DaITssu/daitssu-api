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

    var dueAt: LocalDateTime? = null,

    var startAt: LocalDateTime? = null,

    var submitAt: LocalDateTime? = null,

    var detail: String? = null,

    var comments: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course,
) : BaseEntity() {
    fun update(
        dueAt: LocalDateTime? = null,
        startAt: LocalDateTime? = null,
        submitAt: LocalDateTime? = null,
        detail: String? = null,
        comments: String? = null
    ) {
        dueAt?.also {
            this.dueAt = it
        }
        startAt?.also {
            this.startAt = it
        }
        submitAt?.also {
            this.submitAt = it
        }
        detail?.also {
            this.detail = it
        }
        comments?.also {
            this.comments = it
        }
    }
}
