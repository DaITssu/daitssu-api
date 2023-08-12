package com.example.domain.main.controller

import com.example.common.dto.Response
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.domain.main.service.ArticleService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/daitssu/community/article")
class ArticleController(
    private val articleService: ArticleService
) {
    @GetMapping("/{articleId}")
    fun getArticle(
        @PathVariable articleId: Long
    ): Response<ArticleResponse>
        = Response(data = articleService.getArticle(articleId))

    @PostMapping
    fun writeArticle(
        @RequestBody articlePostRequest: ArticlePostRequest
    ): Response<ArticleResponse>
        = Response(data = articleService.writeArticle(articlePostRequest))
}