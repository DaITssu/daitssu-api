package com.example.common.dto

import com.example.common.enums.UserRole

data class UserTokenDto(
    override val userRole: UserRole,
    override val userId: Long
) : TokenDto
