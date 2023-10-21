package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.service.MyPageService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mypage")
class MyPageController(
    private val myPageService: MyPageService
) {
    @GetMapping("/{userId}/comments")
    fun getComments(
        @PathVariable userId: Long,
    ): Response<List<CommentResponse>> =
        Response(data = myPageService.getComments(userId = userId))

    @PatchMapping("/{userId}/comments")
    fun deleteComments(
        @PathVariable userId: Long,
        @RequestBody request: CommentDeleteRequest
    ): Response<String> {
        myPageService.deleteComments(comments = request.comments)

        return Response(data = "Delete Ok")
    }
}
