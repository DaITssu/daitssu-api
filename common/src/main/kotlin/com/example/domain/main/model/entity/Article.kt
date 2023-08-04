package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import com.example.domain.main.model.entity.User
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "article")
class Article(
    @Column(length = 256)
    @NotNull
    var title: String,

    @Column(length = 2048)
    @NotNull
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    var writer: User,

    @Column(name = "image_url", length = 2048)
    var imageUrl: String? = null,
): BaseEntity()