package com.example.daitssuapi.domain.auth.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.common.security.component.TokenProvider
import com.example.daitssuapi.domain.auth.controller.response.AuthResponse
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.DepartmentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun signIn(
        studentId: String,
    ): AuthResponse {
        val user = userRepository.findByNicknameOrStudentId(
            nickname = studentId,
            studentId = studentId,
        ) ?: throw DefaultException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND)

        // TODO: 람다 스캠 로그인 검증 or exception(비밀번호 오류)

        val accessToken = tokenProvider.createAccessToken(user.id)
        val refreshToken = tokenProvider.createRefreshToken(user.id)

        user.refreshToken = refreshToken.token

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun signUp(
        nickname: String,
        name: String,
        departmentId: Long,
        studentId: String,
        term: Int,
        password: String,
    ): AuthResponse {
        val department = departmentRepository.findByIdOrNull(departmentId)
            ?: throw DefaultException(ErrorCode.DEPARTMENT_NOT_FOUND)

        var user = userRepository.findByStudentId(
            studentId = studentId,
        )

        if (user != null)
            throw DefaultException(ErrorCode.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST)

        // TODO: 스캠 로그인 람다 호출 및 토큰 발급 or exception(비밀번호 오류)
        val ssuToken = ""

        user = userRepository.save(
            User(
                nickname = nickname,
                name = name,
                department = department,
                studentId = studentId,
                imageUrl = null,
                term = term,
                ssuToken = ssuToken,
                refreshToken = "",
            )
        )

        val accessToken = tokenProvider.createAccessToken(user.id)
        val refreshToken = tokenProvider.createRefreshToken(user.id)

        user.refreshToken = refreshToken.token

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun refresh(
        refreshToken: String,
    ): AuthResponse {
        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw DefaultException(ErrorCode.REFRESH_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND)

        val accessToken = tokenProvider.createAccessToken(user.id)
        val newRefreshToken = tokenProvider.createRefreshToken(user.id)

        user.refreshToken = newRefreshToken.token

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = newRefreshToken,
        )
    }
}
