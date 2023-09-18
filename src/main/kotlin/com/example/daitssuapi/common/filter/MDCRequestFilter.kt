package com.example.daitssuapi.common.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.*

@Component
class MDCRequestFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val uuid = UUID.randomUUID().toString()

        MDC.put("uuid", uuid)

        chain?.doFilter(request, response)
    }
}
