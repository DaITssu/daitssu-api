package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.common.DEFAULT_ENCODING
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.model.entity.Comment
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.dto.PageNoticeResponse
import com.example.daitssuapi.domain.notice.model.entity.Notice
import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.Charset

@Service
class NoticeService(
    private val commentRepository: CommentRepository,
    private val noticeRepository: NoticeRepository,
    private val userRepository: UserRepository,
){
    fun getAllNoticeList(
        searchKeyword:String?,
        pageable: Pageable,
    ):Page<NoticeResponse>{
        val notices: Page<Notice>
        if(searchKeyword==null){
            notices = noticeRepository.findAll(pageable)
        }else{
            notices= noticeRepository.findByTitleContaining(searchKeyword=searchKeyword,pageable = pageable)
        }
        return notices.map { NoticeResponse.fromNotice(it) }
    }

    fun getNoticeList(
        category: NoticeCategory,
        searchKeyword: String?,
        pageable: Pageable,

    ):Page<NoticeResponse>{
        val notices : Page<Notice>
        if(searchKeyword==null){
            notices = noticeRepository.findByCategory(category,pageable)
        }else{
            notices = noticeRepository.findByCategoryAndTitleContaining(category,searchKeyword,pageable)
        }
        return notices.map{ NoticeResponse.fromNotice(it)}
    }

    fun getNoticePage(
        id: Long
    ): NoticePageResponse {
        val notice :Notice = noticeRepository.findByIdOrNull(id)
            ?: throw DefaultException(errorCode= ErrorCode.NOTICE_NOT_FOUND)
        return NoticePageResponse.fromNotice(notice)
    }

    fun updateViews( id:Long ) {
        noticeRepository.findByIdOrNull(id)?.apply {
            this.views += 1
        }?.also {
            noticeRepository.save(it)
        } ?:throw DefaultException(ErrorCode.NOTICE_NOT_FOUND)
    }


    @Transactional
    fun writeComment(noticeId: Long, request: CommentWriteRequest): CommentResponse {
        val user = userRepository.findByIdOrNull(request.userId)
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

        return CommentResponse(
            commentId = comment.id,
            userId = comment.writer.id,
            content = comment.content,
            originalCommentId = comment.originalId,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt
        )
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

        return commentRepository.findByNoticeId(noticeId = noticeId).map {
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



