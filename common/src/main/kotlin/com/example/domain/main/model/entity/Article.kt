package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import com.example.domain.main.enums.Topic
import com.example.domain.main.model.entity.User
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

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

    @Column(length = 2048)
    var imageUrl: String? = null,
): BaseEntity()