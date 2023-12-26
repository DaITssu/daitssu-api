package com.example.daitssuapi.domain.article.model.repository

import com.example.daitssuapi.domain.article.model.entity.Scrap
import com.example.daitssuapi.domain.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ScrapRepository : JpaRepository<Scrap, Long> {
    fun findByArticleIdAndUserId(articleId: Long, userId: Long): Scrap?

    fun findByArticleIdAndIsActiveTrue(articleId: Long): List<Scrap>
    
    fun findByUserAndIsActiveTrueOrderByCreatedAtDesc(user: User): List<Scrap>
}
