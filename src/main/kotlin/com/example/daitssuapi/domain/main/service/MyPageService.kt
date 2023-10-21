package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MyPageService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    fun getComments(userId: Long): List<CommentResponse> {
        userRepository.findByIdOrNull(id = userId) ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)

        return commentRepository.findByWriterId(userId = userId).filter {
            !it.isDeleted
        }.map {
            CommentResponse.of(it)
        }
    }

    @Transactional
    fun deleteComments(comments: List<Long>) {
        commentRepository.findAllById(comments).forEach {
            it.isDeleted = true
        }
    }
}
