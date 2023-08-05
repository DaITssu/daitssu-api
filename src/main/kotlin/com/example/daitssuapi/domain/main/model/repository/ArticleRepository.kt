package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.model.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository: JpaRepository<Article, Long> {
}