package com.example.domain.main.dto.response

import com.example.domain.main.dto.AssignmentDto
import com.example.domain.main.dto.VideoDto

class CourseResponse (
    val courseName: String,
) {
    val videos: MutableList<VideoDto> = mutableListOf()
    val assignments: MutableList<AssignmentDto> = mutableListOf()
}