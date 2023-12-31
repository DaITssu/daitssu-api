package com.example.daitssuapi.domain.auth.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.common.security.component.TokenProvider
import com.example.daitssuapi.domain.auth.client.SmartCampusCrawlerClient
import com.example.daitssuapi.domain.auth.client.request.CrawlBaseInformationRequest
import com.example.daitssuapi.domain.auth.client.request.SmartCampusSignInRequest
import com.example.daitssuapi.domain.auth.controller.response.AuthInfoResponse
import com.example.daitssuapi.domain.auth.controller.response.AuthResponse
import com.example.daitssuapi.domain.user.model.entity.User
import com.example.daitssuapi.domain.user.model.repository.DepartmentRepository
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val smartCampusCrawlerClient: SmartCampusCrawlerClient,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository,
) {
    @Transactional
    fun signIn(
        studentId: String,
        password: String,
    ): AuthResponse {
        val user = userRepository.findByStudentId(
            studentId = studentId,
        ) ?: throw DefaultException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND)

        val token =
            try {
                smartCampusCrawlerClient.smartCampusSignIn(
                    smartCampusSignInRequest = SmartCampusSignInRequest(
                        student_id = studentId,
                        password = password,
                    )
                ).token.replace("\"", "")
            } catch (e: Exception) {
                throw DefaultException(ErrorCode.PASSWORD_INCORRECT, HttpStatus.BAD_REQUEST)
            }

        val accessToken = tokenProvider.createAccessToken(user.id)
        val refreshToken = tokenProvider.createRefreshToken(user.id)

        user.ssuToken = token
        user.refreshToken = refreshToken.token

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun getUserInfo(
        studentId: String,
        password: String,
    ): AuthInfoResponse {
        val signInResponse =
            try {
                smartCampusCrawlerClient.smartCampusSignIn(
                    smartCampusSignInRequest = SmartCampusSignInRequest(
                            student_id = studentId,
                            password = password,
                    )
                )
            } catch (e: Exception) {
                throw DefaultException(ErrorCode.PASSWORD_INCORRECT, HttpStatus.BAD_REQUEST)
            }

        return AuthInfoResponse(
            name = signInResponse.name,
            studentId = signInResponse.sIdno,
            term = signInResponse.semester,
        )
    }

    @Transactional
    fun signUp(
        nickname: String,
        name: String,
        departmentId: Long,
        studentId: String,
        term: Int,
    ): AuthResponse {
        val department = departmentRepository.findByIdOrNull(departmentId)
            ?: throw DefaultException(ErrorCode.DEPARTMENT_NOT_FOUND)

        var user = userRepository.findByStudentId(
            studentId = studentId,
        )

        if (user != null)
            throw DefaultException(ErrorCode.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST)

        user = userRepository.save(
            User(
                nickname = nickname,
                name = name,
                department = department,
                studentId = studentId,
                imageUrl = null,
                term = term,
                ssuToken = null,
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

    fun refreshInfo(
        userId: Long,
    ) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND)

        try {
            if (user.ssuToken == null)
                throw DefaultException(ErrorCode.TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND)
            else
                smartCampusCrawlerClient.crawlBaseInformation(
                    crawlBaseInformationRequest = CrawlBaseInformationRequest(user.ssuToken!!)
                )
        } catch (e: Exception) {
            throw DefaultException(ErrorCode.TOKEN_EXPIRED, HttpStatus.BAD_REQUEST)
        }
    }
}
