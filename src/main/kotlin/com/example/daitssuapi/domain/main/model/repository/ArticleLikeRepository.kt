package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.ArticleLike
import com.example.daitssuapi.domain.main.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleLikeRepository : JpaRepository<ArticleLike, Long> {
    fun findByUserAndArticle(
        user: User,
        article: Article
    ): ArticleLike?
}