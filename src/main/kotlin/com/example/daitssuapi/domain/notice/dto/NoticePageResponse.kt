package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.domain.notice.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
@Schema(description = "공지사항 세부정보")
data class NoticePageResponse(
    @Schema(description = "글 id")
    val id: Long?,
    @Schema(description = "글 제목")
    val title: String,
    val departmentId: Int,
    @Schema(description = "글 내용")
    val content: String,
    @Schema(description = "글 카테고리")
    val category: NoticeCategory?,
    @Schema(description = "이미지 주소")
    val imageUrl: String?,
    @Schema(description = "파일 주소")
    val fileUrl: String?,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    @Schema(description = "조회수")
    val views : Int,
) {
    companion object {
        fun fromNotice(notice: Notice): NoticePageResponse {
            return NoticePageResponse(
                id = notice.id,
                title = notice.title,
                departmentId = notice.departmentId,
                content = notice.content,
                category = notice.category,
                imageUrl = notice.imageUrl,
                fileUrl = notice.fileUrl,
                createdAt = notice.createdAt,
                updatedAt = notice.updatedAt,
                views = notice.views,
            )
        }
    }
}