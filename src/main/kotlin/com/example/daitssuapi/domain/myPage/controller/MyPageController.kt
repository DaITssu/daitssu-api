package com.example.daitssuapi.domain.myPage.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.myPage.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.myPage.dto.response.MyArticleResponse
import com.example.daitssuapi.domain.myPage.dto.response.MyAssignmentResponse
import com.example.daitssuapi.domain.myPage.dto.response.MyScrapResponse
import com.example.daitssuapi.domain.myPage.service.MyPageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/articles")
    fun getMyArticle(): Response<List<MyArticleResponse>> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = myPageService.getMyArticle(userId = userId))
    }

    @Operation(
        summary = "내가 스크랩한 글 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/scraps")
    fun getMyScraps(): Response<List<MyScrapResponse>> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = myPageService.getMyScrap(userId = userId))
    }

    @Operation(
        summary = "나의 과제 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/assignments")
    fun getAssignments(
        @RequestParam courseId: Long?
    ): Response<List<MyAssignmentResponse>> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = myPageService.getAssignments(userId = userId, courseId = courseId))
    }
}
