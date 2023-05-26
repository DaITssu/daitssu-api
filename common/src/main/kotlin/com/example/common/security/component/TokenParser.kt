package com.example.common.security.component

import com.example.common.dto.UserTokenDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class TokenParser {
    fun getCustomerTokenDtoOrNull(): UserTokenDto? =
        SecurityContextHolder.getContext().authentication.principal as? UserTokenDto

    fun isAuthorized(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication != null && authentication.isAuthenticated
    }
}
