package com.example.daitssuapi.domain.course.dto.response

data class CourseResponse(
    val id: Long,
    val name: String,
    val term: Int,
    val courseCode: String,
    val videos: List<VideoResponse> = emptyList(),
    val assignments: List<AssignmentResponse> = emptyList()
)
