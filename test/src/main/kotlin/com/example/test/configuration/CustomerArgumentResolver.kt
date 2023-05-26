package com.example.test.configuration

import com.example.common.argument.ArgumentResolver
import com.example.common.enums.UserRole
import com.example.common.security.component.TokenParser
import org.springframework.stereotype.Component

@Component
class CustomerArgumentResolver(
    private val tokenParser: TokenParser,
) : ArgumentResolver {
    override fun resolveIdOrNull(): String? {
        return tokenParser.getCustomerTokenDtoOrNull()?.userId.toString()
    }

    override fun resolveRole(): UserRole {
        return tokenParser.getCustomerTokenDtoOrNull()!!.userRole
    }
}
