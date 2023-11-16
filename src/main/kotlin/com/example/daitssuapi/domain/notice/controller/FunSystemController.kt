package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/funsystem")
class FunSystemController(
    private val funSystemService: FunSystemService

){
    @Operation(
        summary = "펀시스템 공지 전체 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping
    fun getAllFunSystemList(
        @RequestParam searchKeyword:String? = null
    ):Response<List<FunSystemResponse>>{
        return Response(data = funSystemService.getAllFunSystemList(searchKeyword))
    }

    @Operation(
        summary = "카테고리를 이용한 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/{category}") // TODO : 저게 Path로 들어가는게 맞을까요?
    fun getFunSystemListWithCategory(
        @PathVariable category:FunSystemCategory,
        @RequestParam searchKeyword:String? = null,
    ): Response<List<FunSystemResponse>>{

        return Response(data = funSystemService.getFunSystemList(category, searchKeyword))
    }

    @Operation(
        summary = "N페이지의 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/page/{id}") // TODO : 페이지 기준이 없는데 이게 무슨 의미가 있나 싶습니다.
    fun getFunSystemPage(
        @PathVariable id : Long,
    ):Response<FunSystemPageResponse>{
        funSystemService.updateViews(id)
        return Response(data = funSystemService.getFunSystemPage(id))
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
    @PostMapping("/{funSystemId}/comments")
    fun writeComment(
        @PathVariable funSystemId: Long,
        @RequestBody commentWriteRequest: CommentWriteRequest
    ): Response<CommentResponse> = Response(data = funSystemService.writeComment(funSystemId = funSystemId, request = commentWriteRequest))

    @Operation(
        summary = "댓글 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/{funSystemId}/comments")
    fun getComments(
        @PathVariable funSystemId: Long
    ): Response<List<CommentResponse>> = Response(data = funSystemService.getComments(funSystemId = funSystemId))
}
