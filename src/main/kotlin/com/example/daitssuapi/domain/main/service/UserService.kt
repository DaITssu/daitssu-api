package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.response.UserResponse
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUser(userId: Long): UserResponse {
        val user = (userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND, httpStatus = HttpStatus.NOT_FOUND))

        return UserResponse(
            studentId = user.studentId,
            name = user.name,
            nickname = user.nickname ?: throw DefaultException(errorCode = ErrorCode.USER_NICKNAME_MISSING),
            departmentName = user.department.name,
            term = user.term
        )
    }
    @Transactional
    fun updateNickname(userId: Long, nickname:String):String?{

        var user = (userRepository.findByIdOrNull(userId)?.apply{
            this.nickname = nickname
        }?.also {
            userRepository.save(it)
        } ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND, httpStatus = HttpStatus.NOT_FOUND))

        return user.nickname
    }
}
