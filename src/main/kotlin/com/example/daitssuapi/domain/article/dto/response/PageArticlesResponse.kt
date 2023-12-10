package com.example.daitssuapi.domain.article.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(name = "게시글 조회 API response body")
data class PageArticlesResponse(
    val articles: List<ArticleResponse>,
    val totalPages: Int,
) : Serializable
