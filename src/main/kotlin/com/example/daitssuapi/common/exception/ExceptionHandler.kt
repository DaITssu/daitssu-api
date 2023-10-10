package com.example.daitssuapi.common.exception

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.filter.RequestLoggingFilter
import com.example.daitssuapi.common.security.component.TraceIdResolver
import jakarta.servlet.http.HttpServletRequest
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.util.ContentCachingRequestWrapper
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandler(
    private val traceIdResolver: TraceIdResolver,
) {
    private val log: KLogger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception, request: HttpServletRequest): ResponseEntity<Response<Any>> {
        val requestInfo = RequestLoggingFilter.makeRequestInfo(ContentCachingRequestWrapper(request))
        val traceId = traceIdResolver.getTraceIdOrNull()
        val timestamp = LocalDateTime.now()

        lateinit var response: Response<Any>
        var httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        when (exception) {
            is BaseException -> {
                response = Response(
                    traceId = traceId,
                    timestamp = timestamp,
                    code = exception.errorCode.code,
                    message = exception.errorCode.message,
                    data = null
                )
                httpStatus = exception.httpStatus

                log.warn {
                    """
                        {
                            "traceId": $traceId,
                            "timestamp": $timestamp,
                            "request" : $requestInfo,
                            "code" : "${exception.errorCode.code}",
                            "message" : "${exception.errorCode.message}",
                            "status" : "$httpStatus"
                        }
                    """.trimIndent()
                }
            }

            else -> {
                response = Response(
                    traceId = traceId,
                    timestamp = timestamp,
                    code = 1,
                    message = exception.message ?: "",
                    data = null
                )

                log.warn {
                    """
                        {
                            "traceId": $traceId,"traceId": $traceId,
                            "timestamp": $timestamp,
                            "request" : $requestInfo,
                            "message" : "${exception.message?.replace("\"", "'")}",
                            "status" : "$httpStatus"
                        }
                    """.trimIndent()
                }
            }
        }

        request.setAttribute("isLogged", true)

        return ResponseEntity.status(httpStatus).body(response)
    }
}
