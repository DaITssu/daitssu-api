package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.dto.response.PageArticlesResponse
import com.example.daitssuapi.domain.main.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/community/article")
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
    ): Response<ArticleResponse> = Response(data = articleService.getArticle(articleId))

    @Operation(
        summary = "게시글 여러개 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping
    fun pageArticleList(
        @Parameter(
            description = """
<b>[필수]</b> 조회할 Page, Page 당 개수, 정렬 기준입니다. <br />
`page`는 zero-indexed 입니다. <br />
<b>[기본 값]</b><br />
page: 0 <br />
size: 5 <br />
sort: [\"createdAt\"]
            """,
        )
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
        )
        pageable: Pageable,
    ): Response<PageArticlesResponse> {
        val articles = articleService.pageArticleList(pageable)

        return Response(
            data = PageArticlesResponse(
                articles = articles.content,
                totalPages = articles.totalPages,
            )
        )
    }

    @Operation(
        summary = "새로운 게시글 작성",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createArticle(
        @ModelAttribute articleCreateRequest: ArticleCreateRequest
    ) {
        articleService.createArticle(articleCreateRequest)
    }
}
