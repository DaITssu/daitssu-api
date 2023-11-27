package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notice")
class NoticeController(
    private val noticeService: NoticeService,
) {
    @Operation(
        summary = "전체 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping
    fun getAllNoticeList(
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
        @RequestParam searchKeyword: String? = null
    ): Response<Page<NoticeResponse>> {
        return Response(data = noticeService.getAllNoticeList(searchKeyword, pageable))
    }

    @Operation(
        summary = "카테고리를 이용한 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/{category}") // TODO : 저게 Path로 들어가는게 맞을까요?
    fun getNoticeList(
        @PathVariable category: NoticeCategory,
        @RequestParam searchKeyword: String? = null,
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"]) pageable: Pageable, // TODO : 이거 swagger에서 조작 불가
    ): Response<Page<NoticeResponse>> {
        return Response(data = noticeService.getNoticeList(category, searchKeyword, pageable))
    }

    @Operation(
        summary = "N페이지의 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/page/{id}") // TODO : 페이지 기준이 없는데 이게 무슨 의미가 있나 싶습니다.
    fun getNoticePage(
        @PathVariable id: Long,
    ): Response<NoticePageResponse> {
        return Response(data = noticeService.getNoticePage(id))
    }

    @Operation(
        summary = "N페이지의 공지 조회수 업데이트",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PatchMapping("/page/{id}")
    fun updateNoticeView(
        @PathVariable id: Long,
    ) {
        noticeService.updateViews(id)
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
    ): Response<CommentResponse> =
        Response(data = noticeService.writeComment(noticeId = noticeId, request = commentWriteRequest))

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


