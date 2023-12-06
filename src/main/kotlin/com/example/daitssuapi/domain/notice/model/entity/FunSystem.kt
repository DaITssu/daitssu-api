package com.example.daitssuapi.domain.notice.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.domain.notice.enums.FunSystemCategory
import com.example.daitssuapi.common.converter.JsonParsingConverter


import jakarta.persistence.*

@Entity
@Table(name = "notice_fs")
class FunSystem(

    @Column(name = "title", length = 256, nullable = false)
    val title: String,

    @Column(name = "content", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")

    val category: FunSystemCategory,


    @Convert(converter = JsonParsingConverter::class)
    val imageUrl: List<String> = emptyList(),

    @Column(name = "url", nullable = false)
    val url: String, // TODO : 이거는 공지 자체에 대한 URL같은데, Notice에는 그런거 없음

    @Column(name = "views", nullable = false)
    var views: Int,
) : BaseEntity()
