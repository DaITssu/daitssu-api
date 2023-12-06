package com.example.daitssuapi.domain.notice.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.domain.notice.enums.NoticeCategory
import com.example.daitssuapi.common.converter.JsonParsingConverter
import jakarta.persistence.*

@Entity
class Notice(

    @Column(name = "title", length = 1024, nullable = false)
    val title: String,

    @Column(name = "department_id", nullable = false)
    val departmentId: Int,

    @Column(name = "content", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    val category: NoticeCategory,

    @Convert(converter = JsonParsingConverter::class)
    val imageUrl: List<String> = emptyList(),

    @Convert(converter = JsonParsingConverter::class)
    val fileUrl: List<String> = emptyList(),

    @Column(name = "views", nullable = false)
    var views: Int,

    ) : BaseEntity() {


}
