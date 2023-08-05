package com.example.domain.main.dto.response

import com.example.domain.main.model.entity.Assignment
import com.example.domain.main.model.entity.Video

class CourseResponse (
    val name: String,
    val videos: List<VideoResponse>? = null,
    val assignments: List<AssignmentResponse>? = null,
    val term: Int
)