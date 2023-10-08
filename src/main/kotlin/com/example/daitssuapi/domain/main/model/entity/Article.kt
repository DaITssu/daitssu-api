package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.domain.main.enums.Topic
import jakarta.persistence.*

@Entity
@Table(schema = "main")
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

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    var articleImages: MutableSet<ArticleImage> = mutableSetOf(),

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    val scraps: List<Scrap> = emptyList()
) : BaseEntity()
