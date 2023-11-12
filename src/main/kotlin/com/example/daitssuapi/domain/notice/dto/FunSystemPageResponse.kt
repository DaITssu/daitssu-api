package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.domain.notice.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "펀시스템 페이지 세부정보")
data class FunSystemPageResponse (
    @Schema(description = "게시글 id")
    val id: Long?,
    @Schema(description = "글 제목")
    val title: String,
    @Schema(description = "글 내용")
    val content: String,
    @Schema(description = "카테고리")
    val category: FunSystemCategory?,
    @Schema(description = "이미지 주소")
    val imageUrl: String?,
    @Schema(description = "링크")
    val url: String?,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    @Schema(description = "조회수")
    val views : Int,
){
    companion object {
        fun fromFunSystem(funSystem: FunSystem): FunSystemPageResponse {
            return FunSystemPageResponse(
                id = funSystem.id,
                title = funSystem.title,
                content = funSystem.content,
                category = funSystem.category,
                imageUrl = funSystem.imageUrl,
                url = funSystem.url,
                createdAt = funSystem.createdAt,
                updatedAt = funSystem.updatedAt,
                views = funSystem.views,
            )
        }
    }
}