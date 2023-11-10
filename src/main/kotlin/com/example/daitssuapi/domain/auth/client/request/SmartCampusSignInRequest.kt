package com.example.daitssuapi.domain.auth.client.request

data class SmartCampusSignInRequest(
        val student_id: String,
        val password: String,
)
