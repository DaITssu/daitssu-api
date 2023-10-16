package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.dto.response.PageArticlesResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.dto.PageNoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notice")
class NoticeController(
    private val noticeService: NoticeService,
) {

    @GetMapping("/{category}")
    fun getNoticeList(
        @PathVariable category: String,
        @PathVariable pageable: Pageable
    ): Response<Page<NoticeResponse>> =
        Response(data = noticeService.getNoticeList(category, pageable))

    @GetMapping("/page/{id}")
    fun getNoticePage(
        @PathVariable id: Long,
    ): Response<NoticeResponse> =
        Response(data = noticeService.getNoticePage(id))

    @GetMapping
    fun pageNoticeList(
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
        @RequestParam
        inquiry: String? = null,
    ): Response<PageNoticeResponse> {
        val notice = noticeService.pageNoticeList(
            inquiry = inquiry,
            pageable = pageable
        )

        return Response(
            data = notice
        )
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


