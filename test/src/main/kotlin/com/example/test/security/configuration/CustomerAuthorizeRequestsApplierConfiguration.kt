package com.example.test.security.configuration

import com.example.common.security.configuration.SecurityConfiguration
import com.example.common.security.type.AuthorizeRequestsApplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomerAuthorizeRequestsApplierConfiguration {
    @Bean(SecurityConfiguration.AUTHORIZE_REQUEST_APPLIER_BEAN_NAME)
    fun swaggerRestrictedAuthorizeRequestsApplier(): AuthorizeRequestsApplier = {
        it.authorizeHttpRequests()
            .requestMatchers(
                *URLS_AUTHENTICATION,
                *URLS_DOCUMENT,
            )
            .permitAll()
            .requestMatchers(
                *URLS_DEVELOPMENT_ONLY,
                *URLS_MANAGEMENT,
            )
            .authenticated()
            .anyRequest()
            .permitAll()    // login 구현 되면 .authenticated() 로 변경
    }

    companion object {
        private val URLS_AUTHENTICATION = arrayOf(
            "/auth/signUp",
            "/auth/login",
        )
        private val URLS_DEVELOPMENT_ONLY = arrayOf(
            "/dev/**",
        )
        private val URLS_DOCUMENT = arrayOf(
            "/swagger-ui/**",
            "/v3/api-docs/**",
        )
        private val URLS_MANAGEMENT = arrayOf(
            "/",
            "/manage/**",
        )
    }
}
