package com.example.daitssuapi.domain.user.dto.response

data class UserResponse(
    val studentId: String,
    val name: String,
    val nickname: String,
    val departmentName: String,
    val term: Int,
    val imageUrl: String?,
)
