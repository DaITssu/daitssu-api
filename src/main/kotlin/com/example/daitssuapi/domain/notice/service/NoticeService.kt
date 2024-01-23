package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.common.DEFAULT_ENCODING
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.article.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.article.model.entity.Comment
import com.example.daitssuapi.domain.article.model.repository.CommentRepository
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.dto.PageNoticeResponse
import com.example.daitssuapi.domain.notice.model.entity.Notice
import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.Charset

@Service
class NoticeService(
    private val commentRepository: CommentRepository,
    private val noticeRepository: NoticeRepository,
    private val userRepository: UserRepository,
) {
    fun getAllNoticeList(
        searchKeyword: String?,
        pageable: Pageable,
    ): PageNoticeResponse {
        val notices: Page<Notice>
        if (searchKeyword == null) {
            notices = noticeRepository.findAll(pageable)
        } else {
            notices = noticeRepository.findByTitleContaining(searchKeyword = searchKeyword, pageable = pageable)
        }

        return PageNoticeResponse(
            notices = notices.map { NoticeResponse.fromNotice(it) }.content,
            totalPages = notices.totalElements,
        )
    }

    fun getNoticeList(
        category: NoticeCategory,
        searchKeyword: String?,
        pageable: Pageable,
    ): PageNoticeResponse {
        val notices: Page<Notice>
        if (searchKeyword == null) {
            notices = noticeRepository.findByCategory(category, pageable)
        } else {
            notices = noticeRepository.findByCategoryAndTitleContaining(category, searchKeyword, pageable)
        }

        return PageNoticeResponse(
            notices = notices.map { NoticeResponse.fromNotice(it) }.content,
            totalPages = notices.totalElements,
        )
    }

    fun getNoticePage(
        id: Long
    ): NoticePageResponse {
        val notice: Notice = noticeRepository.findByIdOrNull(id)
            ?: throw DefaultException(errorCode = ErrorCode.NOTICE_NOT_FOUND)
        return NoticePageResponse.fromNotice(notice)
    }

    fun updateViews(id: Long) {
        noticeRepository.findByIdOrNull(id)?.apply {
            this.views += 1
        }?.also {
            noticeRepository.save(it)
        } ?: throw DefaultException(ErrorCode.NOTICE_NOT_FOUND)
    }

    @Transactional
    fun writeComment(noticeId: Long, request: CommentWriteRequest, userId: Long): CommentResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        val notice = noticeRepository.findByIdOrNull(noticeId)
            ?: throw DefaultException(errorCode = ErrorCode.NOTICE_NOT_FOUND)

        validateComment(notice = notice, content = request.content, originalCommentId = request.originalCommentId)

        val comment = commentRepository.save(
            Comment(
                writer = user,
                notice = notice,
                content = request.content,
                originalId = request.originalCommentId
            )
        )

        return CommentResponse.of(comment = comment)
    }

    private fun validateComment(notice: Notice, content: String, originalCommentId: Long?) {
        if (512 < content.toByteArray(Charset.forName(DEFAULT_ENCODING)).size) {
            throw DefaultException(errorCode = ErrorCode.COMMENT_TOO_LONG)
        }

        originalCommentId?.apply {
            val originalComment = commentRepository.findByIdOrNull(this)
                ?: throw DefaultException(errorCode = ErrorCode.COMMENT_NOT_FOUND)

            if (originalComment.notice != notice) {
                throw DefaultException(errorCode = ErrorCode.DIFFERENT_NOTICE)
            }
        }
    }

    fun getComments(noticeId: Long): List<CommentResponse> {
        noticeRepository.findByIdOrNull(noticeId)
            ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND)

        return commentRepository.findByNoticeId(noticeId = noticeId).map(CommentResponse::of)
    }
}



