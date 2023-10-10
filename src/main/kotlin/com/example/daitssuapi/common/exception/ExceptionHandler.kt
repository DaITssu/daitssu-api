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
    private val traceId = traceIdResolver.getTraceIdOrNull()

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception, request: HttpServletRequest): ResponseEntity<Response<Any>> {
        val requestInfo = RequestLoggingFilter.makeRequestInfo(ContentCachingRequestWrapper(request))

        lateinit var response: Response<Any>
        var httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        when (exception) {
            is BaseException -> {
                response = Response(code = exception.errorCode.code, message = exception.errorCode.message, data = null)
                httpStatus = exception.httpStatus

                log.warn {
                    """
                        {
                            "traceId": $traceId,
                            "timestamp": ${LocalDateTime.now()},
                            "request" : $requestInfo,
                            "code" : "${exception.errorCode.code}",
                            "message" : "${exception.errorCode.message}",
                            "status" : "$httpStatus"
                        }
                    """.trimIndent()
                }
            }

            else -> {
                response = Response(code = 1, message = "", data = null)

                log.warn {
                    """
                        {
                            "traceId": $traceId,"traceId": $traceId,
                            "timestamp": ${LocalDateTime.now()},
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
