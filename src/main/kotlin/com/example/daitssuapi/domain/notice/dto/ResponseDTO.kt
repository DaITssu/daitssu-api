package com.example.daitssuapi.domain.notice.dto


class ResponseDTO<T> (
    private val error: String, // 에러 메세지 담는다
    private val data : List<T>,
)