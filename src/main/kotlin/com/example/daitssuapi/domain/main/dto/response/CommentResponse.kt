package com.example.daitssuapi.domain.main.dto.response

import com.example.daitssuapi.domain.main.model.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val userId: Long,
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
                content = content,
                originalCommentId = originalId,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
