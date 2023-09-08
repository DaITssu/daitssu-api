package com.example.daitssuapi.domain.course.dto.response

data class CourseResponse(
    val name: String,
    val videos: List<VideoResponse> = emptyList(),
    val assignments: List<AssignmentResponse> = emptyList(),
    val term: Int
)