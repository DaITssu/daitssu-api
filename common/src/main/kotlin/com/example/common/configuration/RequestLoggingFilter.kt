package com.example.common.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class RequestLoggingFilter : OncePerRequestFilter() {
    private val log: KLogger = KotlinLogging.logger {}

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        log.info { makeRequestLog(wrappedRequest) }

        val wrappedResponse = ContentCachingResponseWrapper(response)
        filterChain.doFilter(wrappedRequest, wrappedResponse)

        log.info { makeResponseLog(wrappedResponse) }
    }

    private fun makeRequestLog(request: HttpServletRequest): String {
        val requestHeaders = request.headerNames.toList().associateWith {
            request.getHeader(it)
        }
        val contentAsByteArray = (request as ContentCachingRequestWrapper).contentAsByteArray
        val requestBody = if (contentAsByteArray.isNotEmpty()) String(contentAsByteArray) else ""

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

    private fun makeResponseLog(wrappedResponse: ContentCachingResponseWrapper): String {
        val responseHeaders = wrappedResponse.headerNames.toList().associateWith {
            wrappedResponse.getHeader(it)
        }
        val responseBody = getResponseBody(wrappedResponse.contentAsByteArray)
        wrappedResponse.copyBodyToResponse()

        return """
             {
                 "response" : {
                     "status" : "${wrappedResponse.status}",
                     "headers" : ${jacksonObjectMapper().writeValueAsString(responseHeaders)},
                     "body" : "$responseBody"
                 }
             }
         """.trimIndent()
    }

    private fun getResponseBody(responseBody: ByteArray): String {
        if (responseBody.isEmpty()) {
            return "Internal Error. responseBody is empty"
        }

        val filteredBody = listOf("code", "message", "data")
        val readTree = jacksonObjectMapper().readTree(responseBody)

        val jsonMap = filteredBody.associateWith {
            when (val body = readTree.get(it).toString()) {
                "\"\"" -> ""
                "null" -> null
                else -> body
            }
        }
        return jacksonObjectMapper().writeValueAsString(jsonMap)
    }
}