package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.enums.FunSystemCategory
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/funsystem")
@Tag(name = "FunSystem", description = "펀시스템 API")
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
        @Parameter(
            name = "searchKeyword",
            required = false,
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
        @RequestParam searchKeyword:String? = null
    ):Response<Page<FunSystemResponse>>{
        return Response(data = funSystemService.getAllFunSystemList(searchKeyword,pageable))
    }

    @Operation(
        summary = "카테고리를 이용한 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/category")
    fun getFunSystemListWithCategory(
        @Parameter(
            name = "category",
            description = "펀시스템 카테고리",
            examples = [
                ExampleObject(value = "SUBSCRIPTION", name = "구독"),
                ExampleObject(value = "LEARNING_SKILLS", name = "학습역량"),
                ExampleObject(value = "COMPETITION", name = "공모전/경진대회"),
                ExampleObject(value = "CERTIFICATION", name = "자격증/특강"),
                ExampleObject(value = "STUDENT_ACTIVITIES", name = "학생활동"),
                ExampleObject(value = "STUDY_ABROAD", name = "해외연수/교환학생"),
                ExampleObject(value = "INTERNSHIP", name = "인턴"),
                ExampleObject(value = "VOLUNTEERING", name = "봉사"),
                ExampleObject(value = "EXPERIENTIAL_ACTIVITIES", name = "체험활동"),
                ExampleObject(value = "COUNSELING", name = "심리/상담/진단"),
                ExampleObject(value = "CAREER_SUPPORT", name = "진로지원"),
                ExampleObject(value = "STARTUP_SUPPORT", name = "창업지원"),
                ExampleObject(value = "EMPLOYMENT_SUPPORT", name = "취업지원")
            ]
        )
        @RequestParam category: FunSystemCategory,

        @Parameter(
            name = "searchKeyword",
            description = "검색 키워드 (선택적)",
            required = false,
        )
        @RequestParam searchKeyword:String? = null,
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"]) pageable: Pageable, // TODO : 이거 swagger에서 조작 불가
    ): Response<Page<FunSystemResponse>>{
        return Response(data = funSystemService.getFunSystemList(category, searchKeyword, pageable))
    }

    @Operation(
        summary = "N페이지의 공지 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )

    @GetMapping("/{id}")
    fun getFunSystemPage(
        @PathVariable id : Long,
    ):Response<FunSystemPageResponse>{
        return Response(data = funSystemService.getFunSystemPage(id))
    }
    @Operation(
        summary = "N페이지의 펀시스템 조회수 업데이트",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PatchMapping("/{id}")
    fun updateFunSystemView(
        @PathVariable id : Long,
    ){
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
