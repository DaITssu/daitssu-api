package com.example.common.argument

import com.example.common.enums.UserRole

interface ArgumentResolver {
    fun resolveIdOrNull(): String?

    fun resolveRole(): UserRole
}


