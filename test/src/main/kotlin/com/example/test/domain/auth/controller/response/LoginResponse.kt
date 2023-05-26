package com.example.test.domain.auth.controller.response

import com.example.common.enums.UserRole

class LoginResponse(
    val email: String,
    val username: String,
    val role: UserRole,
    val accessToken: String,
)
