package com.example.common.dto

import com.example.common.enums.UserRole

data class UserDto(
    val email: String,
    val username: String,
    val userRole: UserRole,
)
