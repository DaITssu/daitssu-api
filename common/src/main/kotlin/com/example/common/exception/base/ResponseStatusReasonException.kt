package com.example.common.exception.base

import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException

abstract class ResponseStatusReasonException(
    statusCode: HttpStatusCode,
    val reasonName: String,
    val reasonMessage: String,
) : ResponseStatusException(statusCode, reasonMessage)

