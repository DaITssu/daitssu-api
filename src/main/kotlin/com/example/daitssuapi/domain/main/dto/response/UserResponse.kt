package com.example.daitssuapi.domain.main.dto.response

data class UserResponse(
    val studentId: String,
    val name: String,
    val nickname: String,
    val departmentName: String,
    val term: Int
)
