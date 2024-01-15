package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.converter.JsonParsingConverter
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class CourseNotice(
    val isActive: Boolean = true,

    val name: String,

    val registeredAt: LocalDateTime,

    var views: Int = 0,

    var content: String,

    @Convert(converter = JsonParsingConverter::class)
    var fileUrl: List<String> = emptyList(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course,
) : BaseEntity() {
    fun viewNotice() {
        this.views += 1
    }

    fun update(
        content: String? = null,
        fileUrl: List<String>? = null
    ) {
        content?.also {
            this.content = content
        }
        fileUrl?.also {
            this.fileUrl = fileUrl
        }
    }
}
