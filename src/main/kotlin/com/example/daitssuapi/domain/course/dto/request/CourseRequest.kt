package com.example.daitssuapi.domain.course.dto.request

import org.jetbrains.annotations.NotNull

data class CourseRequest(
    @NotNull
    val name: String,

    @NotNull
    val term: Int
)