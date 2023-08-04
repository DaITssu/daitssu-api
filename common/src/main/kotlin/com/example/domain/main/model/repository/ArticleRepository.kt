package com.example.domain.main.model.repository

import com.example.domain.main.model.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository: JpaRepository<Article, Long> {
}