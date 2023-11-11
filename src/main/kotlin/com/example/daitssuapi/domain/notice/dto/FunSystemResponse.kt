package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
@Schema(description = "펀시스템 리스트 아이템 정보")
data class FunSystemResponse (
    @Schema(description = "글 id")
    val id: Long?,
    @Schema(description = "제목")
    val title: String,
    @Schema(description = "카테고리")
    val category: FunSystemCategory?,
    @Schema(description = "생성 날짜")
    val createdAt: LocalDateTime,
    @Schema(description = "조회수")
    val views : Int,
){
    companion object {
        fun fromFunSystem(funSystem: FunSystem): FunSystemResponse {
            return FunSystemResponse(
                id = funSystem.id,
                title = funSystem.title,
                category = funSystem.category,
                createdAt = funSystem.createdAt,
                views = funSystem.views,
            )
        }
    }
}