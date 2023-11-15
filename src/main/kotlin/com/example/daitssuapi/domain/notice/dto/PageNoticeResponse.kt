package com.example.daitssuapi.domain.notice.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(name = "공지사항 조회 API response body")
data class PageNoticeResponse(
        val notices: List<NoticeResponse>,
        val totalPage: Int,
) : Serializable