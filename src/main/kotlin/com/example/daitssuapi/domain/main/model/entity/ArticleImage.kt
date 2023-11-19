package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class ArticleImage(
    @field:NotNull
    @Column(length = 2048)
    var url: String,

    @field:NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id", nullable = false)
    var article: Article,
) : BaseEntity()
