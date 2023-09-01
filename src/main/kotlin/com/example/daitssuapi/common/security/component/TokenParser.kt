package com.example.daitssuapi.common.security.component

import com.example.daitssuapi.common.dto.TokenDto
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class TokenParser {
    fun getTokenDto(): TokenDto = SecurityContextHolder.getContext().authentication.principal as TokenDto?
        ?: throw DefaultException(ErrorCode.TOKEN_INVALID)

}
