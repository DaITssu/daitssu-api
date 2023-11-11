package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
@Schema(description = "공지사항 리스트 아이템 정보")
data class NoticeResponse(
    @Schema(description = "글 id")
    val id: Long?,
    @Schema(description = "글 제목")
    val title: String,
    @Schema(description = "카테고리")
    val category: NoticeCategory?,
    val createdAt: LocalDateTime,
    @Schema(description = "조회수")
    val views : Int,
) {
    companion object {
        fun fromNotice(notice: Notice): NoticeResponse {
            return NoticeResponse(
                id = notice.id,
                title = notice.title,
                category = notice.category,
                createdAt = notice.createdAt,
                views = notice.views,
            )
        }
    }
}