package com.example.daitssuapi.common.exception

import com.example.daitssuapi.common.enums.ErrorCode
import org.springframework.http.HttpStatus

class DefaultException : BaseException {
    constructor(errorCode: ErrorCode) : super(errorCode = errorCode)

    constructor(errorCode: ErrorCode, httpStatus: HttpStatus) : super(errorCode = errorCode, httpStatus = httpStatus)
}
