package com.example.common.exception.base

import io.micrometer.tracing.Tracer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler(
    private val tracer: Tracer,
) {
    @ExceptionHandler(ResponseStatusReasonException::class)
    fun handleResponseStatusReasonException(ex: ResponseStatusReasonException): ResponseEntity<ExceptionReasonDto> {
        val span = tracer.currentSpan()

        val errorResponse = ExceptionReasonDto(
            traceId = span?.context()?.traceId() ?: "null",
            status = ex.statusCode,
            reason = ex.reasonName,
            message = ex.reasonMessage
        )
        return ResponseEntity.status(ex.statusCode).body(errorResponse)
    }
}
