package com.example.daitssuapi.common.exception

import com.example.daitssuapi.common.enums.ErrorCode
import org.springframework.http.HttpStatus

open class BaseException(
    val errorCode: ErrorCode = ErrorCode.BAD_REQUEST,
    override val message: String = "",
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : Exception()
