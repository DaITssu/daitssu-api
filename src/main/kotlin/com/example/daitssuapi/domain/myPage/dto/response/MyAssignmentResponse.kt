package com.example.daitssuapi.domain.myPage.dto.response

import java.time.LocalDateTime

data class MyAssignmentResponse(
    val id: Long,
    val course: MyCourseSimpleResponse,
    val name: String,
    val dueAt: LocalDateTime? = null,
    val startAt: LocalDateTime? = null,
    val submitAt: LocalDateTime? = null,
    val detail: String? = null,
    val comments: String? = null
)

data class MyCourseSimpleResponse(
    val id: Long,
    val name: String,
    val term: Int,
    val courseCode: String
)

