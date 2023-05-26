package com.example.common.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class RequestLoggingFilterConfiguration {
    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        return filter.also {
            it.setIncludePayload(true)
            it.setIncludeQueryString(true)
            it.setIncludeClientInfo(true)
            it.setIncludeHeaders(true)
            it.setMaxPayloadLength(64000)
        }
    }
}
