package com.example.daitssuapi.domain.article.model.repository

import com.example.daitssuapi.domain.article.model.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByArticleId(articleId: Long): List<Comment>

    fun findByNoticeId(noticeId: Long): List<Comment>

    fun findByFunSystemId(funSystemId: Long): List<Comment>

    fun findByWriterIdOrderByIdDesc(userId: Long): List<Comment>
}
