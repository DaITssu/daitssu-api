package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.model.entity.ArticleImage
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleImageRepository : JpaRepository<ArticleImage, Long> {
}