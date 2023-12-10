package com.example.daitssuapi.domain.article.model.repository

import com.example.daitssuapi.domain.article.model.entity.Article
import com.example.daitssuapi.domain.article.model.entity.ArticleLike
import com.example.daitssuapi.domain.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleLikeRepository : JpaRepository<ArticleLike, Long> {
    fun findByUserAndArticleAndIsActiveTrue(
        user: User,
        article: Article
    ): ArticleLike?
}
