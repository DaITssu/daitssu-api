package com.example.daitssuapi.exception

import com.example.daitssuapi.enums.ErrorCode
import org.springframework.http.HttpStatus

open class BaseException(
    val errorCode: ErrorCode = ErrorCode.BAD_REQUEST,
    override val message: String = "",
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : Exception()
