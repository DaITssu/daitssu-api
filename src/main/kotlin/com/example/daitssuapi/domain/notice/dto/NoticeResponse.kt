package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.notice.model.entity.Notice
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDateTime

data class NoticeResponse(
    val id: Long?,
    val title: String,
    val category: NoticeCategory?,
    val createdAt: LocalDateTime,
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