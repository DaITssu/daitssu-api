package com.example.daitssuapi.domain.notice.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.FunSystemCategory
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
    val category: FunSystemCategory?,

    @Column(name = "image_url")
    val imageUrl: List<String>,

    @Column(name = "url", nullable = false)
    val url: String,

    @Column(name = "views", nullable = false)
    var views: Int,
) : BaseEntity()
