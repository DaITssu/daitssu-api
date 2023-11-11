package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/funsystem")
@Tag(name = "FunSystem", description = "펀시스템 API")
class FunSystemController(
    private val funSystemService: FunSystemService

){
    @Operation(
        summary = "펀시스템 전체 검색 가져오기",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping
    fun getAllFunSystemList(
        @Parameter(
            name = "searchKeyword",
            description = "검색 키워드 (선택적)",
            required = false,
        )
        @RequestParam searchKeyword:String? = null
    ):Response<List<FunSystemResponse>>{
        return Response(data = funSystemService.getAllFunSystemList(searchKeyword))
    }

    @Operation(
        summary = "펀시스템 카테고리 검색으로 가져오기",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/{category}")
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
        @PathVariable category:FunSystemCategory,

        @Parameter(
            name = "searchKeyword",
            description = "검색 키워드 (선택적)",
            required = false,
        )
        @RequestParam searchKeyword:String? = null,
    ): Response<List<FunSystemResponse>>{

        return Response(data = funSystemService.getFunSystemList(category, searchKeyword))
    }

    @Operation(
        summary = "펀시스템 글 내용 가져오기",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/page/{id}")
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

