package com.example.domain.course.dto.response

class CourseResponse (
    val name: String,
    val videos: List<VideoResponse>? = null,
    val assignments: List<AssignmentResponse>? = null,
    val term: Int
)