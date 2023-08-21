package com.example.daitssuapi.common.filter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
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
    private val SWAGGER_URLS = listOf("swagger", "api-docs")

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (SWAGGER_URLS.any { request.requestURI.contains(it) }) {
            filterChain.doFilter(request, response)
            return
        }

        request.setAttribute("isLogged", false)

        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        filterChain.doFilter(wrappedRequest, wrappedResponse)

        wrappedResponse.copyBodyToResponse()
        if (!(request.getAttribute("isLogged") as Boolean)) {
            val requestInfo = makeRequestInfo(wrappedRequest)
            val responseInfo = makeResponseInfo(wrappedResponse)

            log.info {
                """
                    {
                        "request" : $requestInfo,
                        "response" : $responseInfo
                    }
                """.trimIndent()
            }
        }
    }

    companion object {
        fun makeRequestInfo(request: ContentCachingRequestWrapper): String {
            val requestHeaders = request.headerNames.toList().associateWith {
                request.getHeader(it)
            }
            val requestBody = getBody(request.contentAsByteArray)

            return """
                {
                    "client ip" : "${request.remoteAddr}",
                    "uri" : "${request.requestURI}",
                    "headers" : ${jacksonObjectMapper().writeValueAsString(requestHeaders)},
                    "body" : $requestBody
                }
             """.trimIndent()
        }

        fun makeResponseInfo(response: ContentCachingResponseWrapper): String {
            val responseHeaders = response.headerNames.toList().associateWith {
                response.getHeader(it)
            }
            val responseBody = getBody(response.contentAsByteArray)
            response.copyBodyToResponse()

            return """
                 {
                     "status" : "${response.status}",
                     "headers" : ${jacksonObjectMapper().writeValueAsString(responseHeaders)},
                     "body" : $responseBody
                 }
             """.trimIndent()
        }

        private fun getBody(body: ByteArray): String {
            if (body.isEmpty()) {
                return "{}"
            }

            val readTree = jacksonObjectMapper().readTree(body)
            val filteredJson: ObjectNode = jacksonObjectMapper().createObjectNode()

            readTree.fields().forEach { (key, value) ->
                filteredJson.set<JsonNode>(key, value)
            }

            return jacksonObjectMapper().writeValueAsString(filteredJson)
        }
    }
}
