package com.example.daitssuapi.domain.article.dto.request

import com.example.daitssuapi.domain.article.enums.Topic
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

@Schema(description = "커뮤니티 게시글 작성 API request body")
data class ArticleCreateRequest(
    @Schema(
        description = "게시글 주제",
        allowableValues = ["CHAT", "INFORMATION", "QUESTION"]
    )
    val topic: Topic,

    @Schema(description = "게시글 제목")
    val title: String,

    @Schema(description = "게시글 내용")
    val content: String,

    @Schema(description = "게시글 사진 리스트입니다.")
    val images: List<MultipartFile>
)
