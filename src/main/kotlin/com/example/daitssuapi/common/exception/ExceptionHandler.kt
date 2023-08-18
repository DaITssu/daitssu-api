package com.example.daitssuapi.common.exception

import com.example.daitssuapi.common.configuration.RequestLoggingFilter
import com.example.daitssuapi.common.dto.Response
import jakarta.servlet.http.HttpServletRequest
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.util.ContentCachingRequestWrapper

@RestControllerAdvice
class ExceptionHandler() {
    private val log: KLogger = KotlinLogging.logger {}

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
                            "request" : $requestInfo,
                            "code" : "${exception.errorCode}",
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
