package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.model.entity.Scrap
import com.example.daitssuapi.domain.main.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ScrapRepository : JpaRepository<Scrap, Long> {
    fun findByArticleIdAndUserId(articleId: Long, userId: Long): Scrap?
    fun findByArticleIdAndIsActiveTrue(articleId: Long): List<Scrap>
    fun findByUserAndIsActiveTrue(user: User): List<Scrap>
}
