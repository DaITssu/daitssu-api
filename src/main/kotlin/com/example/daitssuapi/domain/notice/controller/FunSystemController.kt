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

    @GetMapping
    fun getAllFunSystemList(
        @RequestParam searchKeyword:String? = null
    ):Response<List<FunSystemResponse>>{
        return Response(data = funSystemService.getAllFunSystemList(searchKeyword))
    }

    @GetMapping("/{category}")
    fun getFunSystemListWithCategory(
        @PathVariable category:FunSystemCategory,
        @RequestParam searchKeyword:String? = null,
    ): Response<List<FunSystemResponse>>{

        return Response(data = funSystemService.getFunSystemList(category, searchKeyword))
    }


    @GetMapping("/page/{id}") //
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

