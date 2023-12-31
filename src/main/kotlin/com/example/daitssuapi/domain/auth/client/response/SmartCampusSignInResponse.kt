package com.example.daitssuapi.domain.auth.client.response

data class SmartCampusSignInResponse(
    val name: String,
    val sIdno: String,
    val department:String,
    val semester: String,
    val token: String,
)
