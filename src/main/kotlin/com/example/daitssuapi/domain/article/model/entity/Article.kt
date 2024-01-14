package com.example.daitssuapi.domain.article.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.converter.JsonParsingConverter
import com.example.daitssuapi.domain.article.enums.Topic
import com.example.daitssuapi.domain.user.model.entity.User
import jakarta.persistence.*

@Entity
class Article(
    @Enumerated(value = EnumType.STRING)
    @Column(length = 16)
    var topic: Topic,

    @Column(length = 256)
    var title: String,

    @Column(length = 2048)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var writer: User,

    @Convert(converter = JsonParsingConverter::class)
    var imageUrl: List<String> ?= emptyList(),

    @OneToMany(mappedBy = "article")
    var likes: MutableSet<ArticleLike> = mutableSetOf(),

    @OneToMany(mappedBy = "article")
    var comments: MutableSet<Comment> = mutableSetOf(),

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    val scraps: List<Scrap> = emptyList()
) : BaseEntity()
