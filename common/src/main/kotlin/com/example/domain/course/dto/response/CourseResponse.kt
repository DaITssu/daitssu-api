package com.example.domain.course.dto.response

class CourseResponse (
    val name: String,
    val videos: List<VideoResponse> = emptyList(),
    val assignments: List<AssignmentResponse> = emptyList(),
    val term: Int
)