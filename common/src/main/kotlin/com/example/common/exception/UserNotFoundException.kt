package com.example.common.exception

import com.example.common.exception.base.ResponseStatusReasonException
import org.springframework.http.HttpStatus

class UserNotFoundException : ResponseStatusReasonException(
    statusCode = HttpStatus.NOT_FOUND,
    reasonName = "USER_NOT_FOUND",
    reasonMessage = "존재하지 않는 사용자입니다.",
)
