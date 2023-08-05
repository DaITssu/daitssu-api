package com.example.domain.main.dto.request

import org.jetbrains.annotations.NotNull

class CourseRequest (
    @NotNull
    val name: String,
    
    @NotNull
    val term: Int
)