package com.example.daitssuapi.domain.article.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.article.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.article.dto.request.ArticleUpdateRequest
import com.example.daitssuapi.domain.article.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.article.dto.response.ArticleResponse
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.article.dto.response.PageArticlesResponse
import com.example.daitssuapi.domain.article.enums.Topic
import com.example.daitssuapi.domain.article.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

// TODO : Article은 ArticleImage Entity가 왜 있는지 모르겠는데, 이거를 임의로 해결하는건 아니라고 판단하여 해당 Controller는 아예 동작 확인 안 했음
@RestController
@RequestMapping("/community/article")
@Tag(name = "article", description = "커뮤니티 게시글 API")
class ArticleController(
    private val articleService: ArticleService,
    private val argumentResolver: ArgumentResolver,
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
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"]) pageable: Pageable,
        @RequestParam inquiry: String?,
    ): Response<PageArticlesResponse> { // TODO : 유저의 nickname이 null이면 예외 파악이 매우 어려움
        val articles = articleService.pageArticleList(
            inquiry = inquiry,
            pageable = pageable
        )

        return Response(
            data = articles
        )
    }

    @Operation(
        summary = "게시글 토픽으로 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/topic")
    fun pageArticleListWithTopic(
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
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"]) pageable: Pageable,
        @RequestParam topic: Topic,
        @RequestParam inquiry: String?,
    ): Response<PageArticlesResponse> { // TODO : 유저의 nickname이 null이면 예외 파악이 매우 어려움
        val articles = articleService.pageArticleListWithTopic(
            inquiry = inquiry,
            pageable = pageable,
            topic = topic,
        )

        return Response(
            data = articles
        )
    }

    @Operation(
        summary = "인기 게시글 조회(24시간)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/popular")
    fun getPopularArticles(): Response<List<ArticleResponse>> {
        val articles = articleService.getPopularArticles()

        return Response(data = articles)
    }

    @Operation(
        summary = "댓글 작성",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/{articleId}/comments")
    fun writeComment(
        @PathVariable articleId: Long,
        @RequestBody commentWriteRequest: CommentWriteRequest
    ): Response<CommentResponse> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = articleService.writeComment(articleId = articleId, request = commentWriteRequest, userId = userId))
    }

    @Operation(
        summary = "댓글 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/{articleId}/comments")
    fun getComments(
        @PathVariable articleId: Long
    ): Response<List<CommentResponse>> = Response(data = articleService.getComments(articleId = articleId))

    @Operation(
        summary = "새로운 게시글 작성",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]) // TODO : image 비어있을 때 미작동
    fun createArticle(
        @ModelAttribute articleCreateRequest: ArticleCreateRequest
    ): Response<String> {
        val userId = argumentResolver.resolveUserId()

        articleService.createArticle(articleCreateRequest = articleCreateRequest, userId = userId)

        return Response(code = 0, message = "OK", data = null)
    }

    @Operation(
        summary = "게시글 수정",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PutMapping("/{articleId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateArticle(
        @Parameter(name = "articleId", description = "게시글 id")
        @PathVariable articleId: Long,
        @Parameter(name = "request", description = "게시글 수정 요청")
        @ModelAttribute articleUpdateRequest: ArticleUpdateRequest
    ): Response<String> {
        val userId = argumentResolver.resolveUserId()

        articleService.updateArticle(userId, articleId, articleUpdateRequest)

        return Response(code = 0, message = "OK", data = null)
    }

    @Deprecated("삭제를 하는 것이 맞는지 확인") // TODO
    @Operation(
        summary = "게시글 삭제",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/{articleId}")
    fun deleteArticle(
        @PathVariable articleId: Long
    ): Response<String> {
        articleService.deleteArticle(articleId)

        return Response(code = 0, message = "OK", data = null)
    }

    @Operation(
        summary = "게시글 좋아요",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/{articleId}/like")
    fun like(
        @PathVariable
        articleId: Long,
    ): Response<Nothing> {
        val userId = argumentResolver.resolveUserId()

        articleService.like(articleId = articleId, userId = userId)

        return Response(code = 0, message = "OK", data = null)
    }

    @Operation(
        summary = "게시글 좋아요 취소",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/{articleId}/dislike")
    fun dislike(
        @PathVariable
        articleId: Long,
    ): Response<Nothing> {
        val userId = argumentResolver.resolveUserId()

        articleService.dislike(articleId = articleId, userId = userId)

        return Response(code = 0, message = "OK", data = null)
    }

    @Operation(
        summary = "게시글 스크랩",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/{articleId}/scrap")
    fun scrapArticle(
        @PathVariable articleId: Long,
        @RequestParam isActive: Boolean
    ): Response<Nothing> {
        val userId = argumentResolver.resolveUserId()

        articleService.scrapArticle(articleId = articleId, userId = userId, isActive = isActive)

        return Response(code = 0, message = "OK", data = null)
    }
}

