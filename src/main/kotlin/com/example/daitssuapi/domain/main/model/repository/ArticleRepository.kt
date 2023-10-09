package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findAllByTitleContainingOrContentContaining(
        title: String,
        content: String,
        pageable: Pageable,
    ): Page<Article>
    fun findByTopicAndTitleContainingOrContentContaining(
        topic: Topic,
        title: String,
        content: String,
        pageable: Pageable,
    ): Page<Article>
    fun findByTopic(
        topic :Topic,
        pageable: Pageable,
    ):Page<Article>
}