package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.springframework.web.bind.annotation.*
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag


@RestController
@RequestMapping("/notice")
@Tag(name = "Notice", description = "공지사항 API")
class NoticeController (
    private val noticeService : NoticeService,
){
    @Operation(

        summary = "전체 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping
    fun getAllNoticeList(
        @RequestParam searchKeyword:String? = null
    ): Response<List<NoticeResponse>>{
        return Response(data = noticeService.getAllNoticeList(searchKeyword))
    }

    @Operation(

        summary = "카테고리를 이용한 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/{category}") // TODO : 저게 Path로 들어가는게 맞을까요?

    fun getNoticeListWithCategory(
        @Parameter(
            name = "category",
            description = "공지사항 카테고리",
            examples = [
                ExampleObject(value = "SUBSCRIPTION", name = "구독"),
                ExampleObject(value = "ACADEMICS", name = "학사"),
                ExampleObject(value = "SCHOLARSHIP", name = "장학"),
                ExampleObject(value = "INTERNATIONAL_EXCHANGE", name = "국제교류"),
                ExampleObject(value = "RECRUITMENT", name = "채용"),
                ExampleObject(value = "INTERNATIONAL_STUDENT", name = "외국인유학생"),
                ExampleObject(value = "TEACHING", name = "교직"),
                ExampleObject(value = "EXTRACURRICULAR", name = "비교과행사"),
                ExampleObject(value = "VOLUNTEERING", name = "봉사"),
                ExampleObject(value = "FACULTY_RECRUITMENT", name = "교원채용"),
                ExampleObject(value = "OTHER", name = "기타"),
                ExampleObject(value = "COVID_19", name = "코로나19"),
                ExampleObject(value = "UNDERGRADUATE", name = "학부")
            ]
        )
        @PathVariable category: NoticeCategory,
        @RequestParam searchKeyword:String? = null,
    ): Response<List<NoticeResponse>>{
        return Response(data = noticeService.getNoticeList(category, searchKeyword))
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
        noticeService.updateViews(id)
        return Response(data = noticeService.getNoticePage(id))
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


