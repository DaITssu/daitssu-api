package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.service.ArticleService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/daitssu/community/article")
class ArticleController(
    private val articleService: ArticleService
) {
    @GetMapping("/{id}")
    fun getArticle(
        @PathVariable id: Long
    ): Response<ArticleResponse>
        = Response(data = articleService.getArticle(id))

    @PostMapping
    fun writeArticle(
        @RequestBody articlePostRequest: ArticlePostRequest
    ): Response<ArticleResponse>
        = Response(data = articleService.writeArticle(articlePostRequest))
}