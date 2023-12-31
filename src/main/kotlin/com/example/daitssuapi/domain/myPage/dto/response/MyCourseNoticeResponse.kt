package com.example.daitssuapi.domain.myPage.dto.response

import java.time.LocalDateTime

data class MyCourseNoticeResponse(
    val id: Long,
    val course: MyCourseSimpleResponse,
    val name: String,
    val isActive: Boolean,
    val registeredAt: LocalDateTime? = null,
    val views: Int,
    val content: String,
    val fileUrl: List<String>
)
