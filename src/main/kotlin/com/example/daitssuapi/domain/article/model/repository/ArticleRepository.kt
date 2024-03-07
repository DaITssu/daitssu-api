package com.example.daitssuapi.domain.article.model.repository

import com.example.daitssuapi.domain.article.enums.Topic
import com.example.daitssuapi.domain.article.model.entity.Article
import com.example.daitssuapi.domain.user.model.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findAllByTitleContainingOrContentContaining(
        title: String,
        content: String,
        pageable: Pageable,
    ): Page<Article>

    fun findByTopic(
        topic :Topic,
        pageable: Pageable,
    ):Page<Article>

    @Query("SELECT a FROM Article a WHERE (a.topic = :topic) AND (a.title LIKE %:title% OR a.content LIKE %:content%)")
    fun findByTitleContainingOrContentContainingAndTopic(
        title: String,
        content: String,
        pageable: Pageable,
        topic: Topic,
    ): Page<Article>

    fun findAllByCreatedAtIsGreaterThanEqual(
        createdAt: LocalDateTime
    ): List<Article>

    fun findAllByWriterOrderByCreatedAtDesc(writer: User): List<Article>
}
