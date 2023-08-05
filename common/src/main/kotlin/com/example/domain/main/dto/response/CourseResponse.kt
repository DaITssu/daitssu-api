package com.example.domain.main.dto.response

import com.example.domain.main.model.entity.Assignment
import com.example.domain.main.model.entity.Video

class CourseResponse (
    val courseName: String,
    val videos: MutableList<Video> = mutableListOf(),
    val assignments: MutableList<Assignment> = mutableListOf(),
    val term: Int
)