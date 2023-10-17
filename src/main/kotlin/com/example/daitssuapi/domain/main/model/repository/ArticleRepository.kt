package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findAllByTitleContainingOrContentContaining(
        title: String,
        content: String,
        pageable: Pageable,
    ): Page<Article>
    
    fun findAllByWriter(writer: User) : List<Article>
}