package com.example.daitssuapi.domain.main.dto.request;

import com.example.daitssuapi.domain.main.enums.Topic
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "커뮤니티 게시글 수정 API request body")
data class ArticleUpdateRequest(
        @Schema(description = "게시글 수정을 요청한 유저의 ID")
        val userId: Long,

        @Schema(description = "게시글 주제 (수정할 경우)",
                allowableValues = ["CHAT", "INFORMATION", "QUESTION"])
        val topic : Topic,

        @Schema(description = "게시글 제목 (수정할 경우)")
        val title: String?,

        @Schema(description = "게시글 내용 (수정할 경우)")
        val content: String?,

        @Schema(description = "게시글 사진 리스트 (수정할 경우)")
        val images:List<MultipartFile>?
)

