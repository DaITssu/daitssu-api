package com.example.daitssuapi.domain.article.dto.response

import com.example.daitssuapi.domain.article.model.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val userId: Long,
    val nickname: String? = null,
    val content: String,
    val originalCommentId: Long? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun of(comment: Comment): CommentResponse = with(comment) {
            CommentResponse(
                commentId = id,
                userId = writer.id,
                nickname = writer.nickname,
                content = content,
                originalCommentId = originalId,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
