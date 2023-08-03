package com.example.common.exception

import com.example.common.dto.Response
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler() {
    private val log: KLogger = KotlinLogging.logger {}
    
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Response<Any>> {
        lateinit var response: Response<Any>
        var httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        
        when (exception) {
            is BaseException -> {
                response = Response(code = exception.errorCode.code, message = exception.errorCode.message, data = null)
                httpStatus = exception.httpStatus
                log.warn("${exception.errorCode} + $httpStatus")
            }
            
            else -> {
                response = Response(code = 1, message = "", data = null)
                log.error("${exception.message}")
            }
        }
        
        return ResponseEntity.status(httpStatus).body(response)
    }
}