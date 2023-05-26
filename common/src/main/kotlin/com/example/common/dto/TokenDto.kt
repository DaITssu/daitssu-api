package com.example.common.dto

import com.example.common.enums.UserRole

interface TokenDto {
    val userRole: UserRole
    val userId: Long
}
