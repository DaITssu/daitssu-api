package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import java.time.LocalDateTime

data class NoticePageResponse(
    val id: Long?,
    val title: String,
    val departmentId: Int,
    val content: String,
    val category: NoticeCategory?,
    val imageUrl: List<String>,
    val fileUrl: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
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
