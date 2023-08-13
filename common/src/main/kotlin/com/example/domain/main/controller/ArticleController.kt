package com.example.domain.main.controller

import com.example.common.dto.Response
import com.example.domain.main.dto.request.ArticleWriteRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.domain.main.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/daitssu/community/article")
@Tag(name = "article", description = "커뮤니티 게시글 API")
class ArticleController(
    private val articleService: ArticleService
) {
    @Operation(
        summary = "게시글 단일 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/{articleId}")
    fun getArticle(
        @Parameter(name = "articleId", description = "게시글 id")
        @PathVariable articleId: Long
    ): Response<ArticleResponse>
        = Response(data = articleService.getArticle(articleId))

    @Operation(
        summary = "새로운 게시글 작성",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping
    fun writeArticle(
        @RequestBody articleWriteRequest: ArticleWriteRequest
    ): Response<ArticleResponse>
        = Response(data = articleService.writeArticle(articleWriteRequest))
}