package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.main.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.dto.response.ServiceNoticeResponse
import com.example.daitssuapi.domain.main.service.MyPageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mypage")
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
    @Operation(
        summary = "서비스 공지사항 전체 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/service-notice")
    fun getServiceNotice(): Response<List<ServiceNoticeResponse>> =
        Response(data = myPageService.getServiceNotice())


}
