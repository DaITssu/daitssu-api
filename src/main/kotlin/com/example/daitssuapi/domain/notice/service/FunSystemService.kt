package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.common.DEFAULT_ENCODING
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.model.entity.Comment
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import com.example.daitssuapi.domain.notice.model.repository.FunSystemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.Charset

@Service
class FunSystemService(
    private val funSystemRepository: FunSystemRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    fun getFunSystemList(
        category: String
    ): List<FunSystemResponse> {
        val funSystems: List<FunSystem>

        if (category == "ALL")
            funSystems = funSystemRepository.findAll()
        else {
            val funSystemCategory = FunSystemCategory.fromCode(category)
            if (funSystemCategory != null) {
                funSystems = funSystemRepository.findByCategory(funSystemCategory)
            } else {
                throw DefaultException(errorCode = ErrorCode.INVALID_CATEGORY)
            }
        }
        return funSystems.map { FunSystemResponse.fromFunSystem(it) }
    }

    fun getFunSystemPage(
        id: Long
    ): FunSystemResponse {
        val funSystem: FunSystem = funSystemRepository.findByIdOrNull(id)
            ?: throw DefaultException(errorCode = ErrorCode.FUNSYSTEM_NOT_FOUND)

        return FunSystemResponse.fromFunSystem(funSystem)
    }

    @Transactional
    fun writeComment(funSystemId: Long, request: CommentWriteRequest): CommentResponse {
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        val funSystem = funSystemRepository.findByIdOrNull(funSystemId)
            ?: throw DefaultException(errorCode = ErrorCode.FUNSYSTEM_NOT_FOUND)

        validateComment(funSystem = funSystem, content = request.content, originalCommentId = request.originalCommentId)

        val comment = commentRepository.save(Comment(
            writer = user,
            funSystem = funSystem,
            content = request.content,
            originalId = request.originalCommentId
        ))

        return CommentResponse(
            commentId = comment.id,
            userId = comment.writer.id,
            content = comment.content,
            originalCommentId = comment.originalId,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt
        )
    }

    private fun validateComment(funSystem: FunSystem, content: String, originalCommentId: Long?) {
        if (512 < content.toByteArray(Charset.forName(DEFAULT_ENCODING)).size) {
            throw DefaultException(errorCode = ErrorCode.COMMENT_TOO_LONG)
        }

        originalCommentId?.apply {
            val originalComment = commentRepository.findByIdOrNull(this)
                ?: throw DefaultException(errorCode = ErrorCode.COMMENT_NOT_FOUND)

            if (originalComment.funSystem != funSystem) {
                throw DefaultException(errorCode = ErrorCode.DIFFERENT_FUNSYSTEM)
            }
        }
    }

    fun getComments(funSystemId: Long): List<CommentResponse> {
        funSystemRepository.findByIdOrNull(funSystemId)
            ?: throw DefaultException(errorCode = ErrorCode.FUNSYSTEM_NOT_FOUND)

        return commentRepository.findByFunSystemId(funSystemId = funSystemId).map {
            CommentResponse(
                commentId = it.id,
                userId = it.writer.id,
                content = it.content,
                originalCommentId = it.originalId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}
