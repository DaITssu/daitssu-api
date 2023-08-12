package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.service.ArticleService
import com.example.daitssuapi.domain.main.dto.request.ArticleWritingRequest
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
        @RequestBody articleWritingRequest: ArticleWritingRequest
    ): Response<ArticleResponse>
        = Response(data = articleService.writeArticle(articleWritingRequest))
}