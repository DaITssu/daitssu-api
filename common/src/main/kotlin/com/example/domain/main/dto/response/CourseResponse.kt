package com.example.domain.main.dto.response

import com.example.domain.main.model.entity.Assignment
import com.example.domain.main.model.entity.Video

class CourseResponse (
    val name: String,
    val videos: MutableList<Video>? = null,
    val assignments: MutableList<Assignment>? = null,
    val term: Int
)