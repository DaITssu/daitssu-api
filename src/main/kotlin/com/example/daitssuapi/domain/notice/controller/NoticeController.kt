package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notice")
class NoticeController(
    private val noticeService: NoticeService,
) {

    @GetMapping("/{category}")
    fun getNoticeList(
        @PathVariable category: String
    ): Response<List<NoticeResponse>> =
        Response(data = noticeService.getNoticeList(category))

    @GetMapping("/page/{id}")
    fun getNoticePage(
        @PathVariable id: Long,
    ): Response<NoticeResponse> =
        Response(data = noticeService.getNoticePage(id))

    @Operation(
        summary = "댓글 작성",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/{noticeId}/comments")
    fun writeComment(
        @PathVariable noticeId: Long,
        @RequestBody commentWriteRequest: CommentWriteRequest
    ): Response<CommentResponse> = Response(data = noticeService.writeComment(noticeId = noticeId, request = commentWriteRequest))

    @Operation(
        summary = "댓글 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/{noticeId}/comments")
    fun getComments(
        @PathVariable noticeId: Long
    ): Response<List<CommentResponse>> = Response(data = noticeService.getComments(noticeId = noticeId))
}


