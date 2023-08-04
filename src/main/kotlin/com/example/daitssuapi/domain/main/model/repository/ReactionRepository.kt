package com.example.domain.main.model.repository

import com.example.domain.main.model.entity.Reaction
import org.springframework.data.jpa.repository.JpaRepository

interface ReactionRepository: JpaRepository<Reaction, Long> {
}