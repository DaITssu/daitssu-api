package com.example.common.exception

import com.example.common.exception.base.ResponseStatusReasonException
import org.springframework.http.HttpStatus

class JwtTokenExpiredException : ResponseStatusReasonException(
    statusCode = HttpStatus.UNAUTHORIZED,
    reasonName = "TOKEN_EXPIRED",
    reasonMessage = "토큰이 만료되었습니다",
)
