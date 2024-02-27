package com.example.daitssuapi.domain.article.dto.response

import com.example.daitssuapi.domain.article.enums.Topic
import com.example.daitssuapi.domain.article.model.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val userId: Long,
    val nickname: String? = null,
    val content: String,
    val originalCommentId: Long? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val title: String? = null,
    val topic: Topic? = null,
    val articleId: Long? = null,
    val noticeId: Long? = null,
    val funSystemId: Long? = null
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
                updatedAt = updatedAt,
                title = when{
                    article != null -> article?.title
                    notice != null -> notice?.title
                    funSystem != null -> funSystem?.title
                    else -> null
                            },
                topic = article?.topic,
                articleId = article?.id,
                noticeId = notice?.id,
                funSystemId = funSystem?.id
            )
        }
    }
}
