package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.*

@Entity
@Table(schema = "main")
class Reaction(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    val article: Article,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
): BaseEntity()