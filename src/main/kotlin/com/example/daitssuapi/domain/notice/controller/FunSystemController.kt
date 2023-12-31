package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.article.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/funsystem")
class FunSystemController(
    private val funSystemService: FunSystemService,
    private val argumentResolver: ArgumentResolver
) {
    @Operation(
        summary = "펀시스템 공지 전체 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping
    fun getAllFunSystemList(
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
        @RequestParam searchKeyword: String? = null
    ): Response<Page<FunSystemResponse>> {
        return Response(data = funSystemService.getAllFunSystemList(searchKeyword, pageable))
    }

    @Operation(
        summary = "카테고리를 이용한 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/category") // TODO : 저게 Path로 들어가는게 맞을까요? -> 제가 request param으로 변경했습니다 참고해주세요
    fun getFunSystemListWithCategory(
        @RequestParam category: FunSystemCategory,
        @RequestParam searchKeyword: String? = null,
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"]) pageable: Pageable, // TODO : 이거 swagger에서 조작 불가
    ): Response<Page<FunSystemResponse>> {
        return Response(data = funSystemService.getFunSystemList(category, searchKeyword, pageable))
    }

    @Operation(
        summary = "N페이지의 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/page/{id}") // TODO : 페이지 기준이 없는데 이게 무슨 의미가 있나 싶습니다.
    fun getFunSystemPage(
        @PathVariable id: Long,
    ): Response<FunSystemPageResponse> {
        return Response(data = funSystemService.getFunSystemPage(id))
    }

    @Operation(
        summary = "N페이지의 펀시스템 조회수 업데이트",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PatchMapping("/page/{id}")
    fun updateFunSystemView(
        @PathVariable id: Long,
    ) {
        funSystemService.updateViews(id)
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
    ): Response<CommentResponse> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = funSystemService.writeComment(funSystemId = funSystemId, request = commentWriteRequest, userId = userId))
    }

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
