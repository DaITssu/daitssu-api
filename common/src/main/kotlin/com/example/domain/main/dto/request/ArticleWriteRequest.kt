package com.example.domain.main.dto.request

import com.example.domain.main.enums.Topic
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "커뮤니티 게시글 작성 API request body")
data class ArticleWriteRequest(
    @Schema(
        description = "게시글 주제",
        allowableValues = ["CHAT", "INFORMATION", "QUESTION"]
    )
    val topic: Topic,

    @Schema(description = "게시글 제목")
    val title: String,

    @Schema(description = "게시글 내용")
    val content: String,

    @Schema(description = "작성자 닉네임")
    val nickname: String? = null
//    val studentId: Int,
//    val images, // image 데이터
)