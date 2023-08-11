package com.example.daitssuapi.security.configuration

import com.example.daitssuapi.security.type.AuthorizeRequestsApplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthorizeRequestsApplierConfiguration {
    @Bean(SecurityConfiguration.AUTHORIZE_REQUEST_APPLIER_BEAN_NAME)
    fun swaggerRestrictedAuthorizeRequestsApplier(): AuthorizeRequestsApplier = {
        it.authorizeHttpRequests()
            .requestMatchers(
                *URLS_AUTHENTICATION,
                *URLS_DOCUMENT,
            )
            .permitAll()
            .anyRequest()
            .authenticated()
    }

    companion object {
        private val URLS_AUTHENTICATION = arrayOf(
            "/auth/**",
        )
        private val URLS_DOCUMENT = arrayOf(
            "/swagger-ui/**",
            "/v3/api-docs/**",
        )
    }
}
