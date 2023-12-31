package com.example.daitssuapi.domain.course.dto.response

import com.example.daitssuapi.domain.course.model.entity.CourseNotice
import java.time.LocalDateTime

data class CourseNoticeResponse(
    val id: Long,
    val courseId: Long,
    val isActive: Boolean,
    val views: Int,
    val content: String,
    val fileUrl: List<String>,
    val registeredAt: LocalDateTime
) {
    companion object {
        fun of(notice: CourseNotice) = with(notice) {
            CourseNoticeResponse(
                id = id,
                courseId = course.id,
                isActive = isActive,
                views = views,
                content = content,
                fileUrl = fileUrl,
                registeredAt = registeredAt
            )
        }
    }
}
