package com.example.daitssuapi.security.component

import org.springframework.stereotype.Component

@Component
class ArgumentResolver(
    private val tokenParser: TokenParser,
) {
    fun resolveUserId(): Long {
        return tokenParser.getTokenDto().userId
    }
}

