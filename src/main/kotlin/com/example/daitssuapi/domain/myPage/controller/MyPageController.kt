package com.example.daitssuapi.domain.myPage.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.myPage.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.myPage.dto.response.MyArticleResponse
import com.example.daitssuapi.domain.myPage.service.MyPageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/myPage")
class MyPageController(
    private val myPageService: MyPageService,
    private val argumentResolver: ArgumentResolver
) {
    @Operation(
        summary = "작성한 댓글 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/comments")
    fun getComments(): Response<List<CommentResponse>> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = myPageService.getComments(userId = userId))
    }

    @Operation(
        summary = "작성한 댓글 삭제",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PatchMapping("/comments")
    fun deleteComments(
        @RequestBody request: CommentDeleteRequest
    ): Response<String> {
        val userId = argumentResolver.resolveUserId()

        // TODO: userId를 사용하지 않고 댓글을 지우는?
        myPageService.deleteComments(commentIds = request.commentIds)

        return Response(data = "Delete Ok")
    }

    @GetMapping("/{userId}/articles")
    fun getMyArticle(
        @PathVariable("userId") userId: Long
    ): Response<List<MyArticleResponse>> =
        Response(data = myPageService.getMyArticle(userId = userId))
}
