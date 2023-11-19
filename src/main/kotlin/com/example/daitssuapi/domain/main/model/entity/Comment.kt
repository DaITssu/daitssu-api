package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import com.example.daitssuapi.domain.notice.model.entity.Notice
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Comment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var writer: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    var article: Article? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    var notice: Notice? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_fs_id")
    var funSystem: FunSystem? = null,

    val content: String,

    val originalId: Long? = null,

    var isDeleted: Boolean = false
) : BaseEntity()
