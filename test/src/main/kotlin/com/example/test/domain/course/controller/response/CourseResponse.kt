package com.example.test.domain.course.controller.response

import com.example.test.domain.course.dto.AssignmentDto
import com.example.test.domain.course.dto.VideoDto

class CourseResponse (
    val courseName: String,
) {
    val videos: MutableList<VideoDto> = mutableListOf()
    val assignments: MutableList<AssignmentDto> = mutableListOf()
}