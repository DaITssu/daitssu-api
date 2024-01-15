package com.example.daitssuapi.domain.user.model.repository

import com.example.daitssuapi.domain.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByStudentId(studentId: String): User?

    fun findByNickname(nickname: String?): User?

    fun findByRefreshToken(refreshToken: String): User?

    fun findByNicknameOrStudentId(
        nickname: String,
        studentId: String,
    ): User?

    fun existsByNickname(
        nickname: String,
    ): Boolean
}
