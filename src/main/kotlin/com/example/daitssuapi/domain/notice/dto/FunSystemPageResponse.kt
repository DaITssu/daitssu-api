package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import java.time.LocalDateTime

data class FunSystemPageResponse (
    val id: Long?,
    val title: String,
    val content: String,
    val category: FunSystemCategory?,
    val imageUrl: String?,
    val url: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
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