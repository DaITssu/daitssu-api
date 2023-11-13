package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.service.MyPageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mypage")
class MyPageController(
    private val myPageService: MyPageService
) {
    @Operation(
        summary = "작성한 댓글 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/{userId}/comments")
    fun getComments(
        @PathVariable userId: Long,
    ): Response<List<CommentResponse>> =
        Response(data = myPageService.getComments(userId = userId))

    @Operation(
        summary = "작성한 댓글 삭제",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PatchMapping("/{userId}/comments")
    fun deleteComments(
        @PathVariable userId: Long,
        @RequestBody request: CommentDeleteRequest
    ): Response<String> {
        myPageService.deleteComments(commentIds = request.commentIds)

        return Response(data = "Delete Ok")
    }
}
