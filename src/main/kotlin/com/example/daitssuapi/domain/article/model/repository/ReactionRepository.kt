package com.example.daitssuapi.domain.article.model.repository

import com.example.daitssuapi.domain.article.model.entity.Reaction
import org.springframework.data.jpa.repository.JpaRepository

interface ReactionRepository : JpaRepository<Reaction, Long> {
}
