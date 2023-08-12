package com.example.daitssuapi.common.dto

data class Response<T>(
    val code: Int = 0,
    val message: String = "",
    val data: T?
)
