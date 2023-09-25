package com.example.daitssuapi.domain.notice.dto

import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import java.time.LocalDateTime

data class FunSystemResponse (
    val id: Long?,
    val title: String,
    val category: FunSystemCategory?,
    val createdAt: LocalDateTime,
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