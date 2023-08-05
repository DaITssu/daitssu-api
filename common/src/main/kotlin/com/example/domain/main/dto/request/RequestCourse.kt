package com.example.domain.main.dto.request

import org.jetbrains.annotations.NotNull

class RequestCourse (
    @NotNull
    val name: String,
    
    @NotNull
    val term: Int
)