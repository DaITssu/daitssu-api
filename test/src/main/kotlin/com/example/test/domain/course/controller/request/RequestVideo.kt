package com.example.test.domain.course.controller.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "강의 등록 API Request Body입니다. ")
class RequestVideo (
    @Schema(description = " [필수] 강의 id입니다. ")
    val courseId: Long,
    @Schema(description = " [필수] 강의 이름입니다. ")
    val name: String
)