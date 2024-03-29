package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.common.DEFAULT_ENCODING
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.article.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.article.model.entity.Comment
import com.example.daitssuapi.domain.article.model.repository.CommentRepository
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.dto.PageFunSystemResponse
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import com.example.daitssuapi.domain.notice.model.repository.FunSystemRepository
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.Charset

@Service
class FunSystemService(
    private val funSystemRepository: FunSystemRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {
    fun getAllFunSystemList(
        searchKeyword: String?,
        pageable: Pageable,
    ): PageFunSystemResponse { //모든 펀시스템 가져오기
        val funSystems: Page<FunSystem>
        if (searchKeyword == null) {
            funSystems = funSystemRepository.findAll(pageable)
        } else {
            funSystems = funSystemRepository.findByTitleContaining(searchKeyword, pageable)
        }

        return PageFunSystemResponse(
            funSystems = funSystems.map { FunSystemResponse.fromFunSystem(it) }.content,
            totalPages = funSystems.totalElements,
        )
    }

    fun getFunSystemList(
        //category 포함 가져오기
        category: FunSystemCategory,
        searchKeyword: String?,
        pageable: Pageable,
    ): PageFunSystemResponse {
        val funSystems: Page<FunSystem>
        if (searchKeyword == null) {
            funSystems = funSystemRepository.findByCategory(category, pageable)
        } else {
            funSystems = funSystemRepository.findByCategoryAndTitleContaining(category, searchKeyword, pageable)
        }

        return PageFunSystemResponse(
            funSystems = funSystems.map { FunSystemResponse.fromFunSystem(it) }.content,
            totalPages = funSystems.totalElements,
        )
    }

    fun getFunSystemPage(
        id: Long
    ): FunSystemPageResponse {
        val funSystem: FunSystem = funSystemRepository.findByIdOrNull(id)
            ?: throw DefaultException(ErrorCode.FUNSYSTEM_NOT_FOUND)
        return FunSystemPageResponse.fromFunSystem(funSystem)
    }

    fun updateViews(id: Long) {
        funSystemRepository.findByIdOrNull(id)?.apply {
            this.views += 1
        }?.also {
            funSystemRepository.save(it)
        } ?: throw DefaultException(ErrorCode.FUNSYSTEM_NOT_FOUND)
    }

    @Transactional
    fun writeComment(funSystemId: Long, request: CommentWriteRequest, userId: Long): CommentResponse {
        val user = userRepository.findByIdOrNull(userId)
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

        return CommentResponse.of(comment = comment)
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

        return commentRepository.findByFunSystemId(funSystemId = funSystemId).map(CommentResponse::of)
    }
}
