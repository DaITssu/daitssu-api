package com.example.common.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class RequestLoggingFilter : OncePerRequestFilter() {
    private val log: KLogger = KotlinLogging.logger {}

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        log.info { makeRequestLog(request) }

        filterChain.doFilter(request, response)

        log.info { makeResponseLog(response) }
    }

    private fun makeRequestLog(request: HttpServletRequest): String {
        val requestHeaders = request.headerNames.toList().associateWith {
            request.getHeader(it)
        }
        val requestBody = request.reader.lines().toList().joinToString("\n")

        return """
            {
                "request" : {
                    "uri" : "${request.requestURI}",
                    "headers" : ${jacksonObjectMapper().writeValueAsString(requestHeaders)},
                    "body" : "$requestBody"
                }
            }
        """.trimIndent()
    }

    private fun makeResponseLog(response: HttpServletResponse): String {
        val wrappedResponse = ContentCachingResponseWrapper(response)
        val responseHeaders = wrappedResponse.headerNames.toList().associateWith {
            wrappedResponse.getHeader(it)
        }
        val responseBody = getResponseBody(wrappedResponse.contentAsByteArray)

        return """
            {
                "request" : {
                    "status" : "${wrappedResponse.status}",
                    "headers" : ${jacksonObjectMapper().writeValueAsString(responseHeaders)},
                    "body" : "$responseBody"
                }
            }
        """.trimIndent()
    }

    private fun getResponseBody(responseBody: ByteArray): String {
        if (responseBody.isEmpty()) {
            return ""
        }

        val filteredBody = listOf("code", "message")
        val readTree = jacksonObjectMapper().readTree(responseBody)

        val jsonMap = filteredBody.associateWith {
            readTree.get(it).toString()
        }
        return jacksonObjectMapper().writeValueAsString(jsonMap)
    }
}
