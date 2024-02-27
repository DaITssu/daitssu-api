package com.example.daitssuapi.domain.auth.controller.response

data class AuthInfoResponse(
    val name: String,
    val studentId: String,
    val term: String,
    val department: String,
)
