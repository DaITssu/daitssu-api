package com.example.common.exception

import com.example.common.exception.base.ResponseStatusReasonException
import org.springframework.http.HttpStatus

class UserAlreadyExistException : ResponseStatusReasonException(
    statusCode = HttpStatus.CONFLICT,
    reasonName = "USER_ALREADY_EXIST",
    reasonMessage = "이미 존재하는 사용자입니다.",
)
