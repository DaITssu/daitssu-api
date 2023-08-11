package com.example.daitssuapi.security.component

import com.example.daitssuapi.dto.TokenDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class TokenParser {
    fun getTokenDto(): TokenDto =
        SecurityContextHolder.getContext().authentication.principal as TokenDto
}
