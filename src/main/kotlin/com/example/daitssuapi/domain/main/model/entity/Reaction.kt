package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "reaction")
class Reaction(
    @ManyToOne
    @JoinColumn(name = "article_id")
    val article: Article,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
): BaseEntity()