package com.example.common.exception

import com.example.common.exception.base.ResponseStatusReasonException
import org.springframework.http.HttpStatus

class ColorFormatInvalidException : ResponseStatusReasonException(
    statusCode = HttpStatus.BAD_REQUEST,
    reasonName = "COLOR_FORMAT_INVALID",
    reasonMessage = "잘못된 색상 형식입니다.",
)
