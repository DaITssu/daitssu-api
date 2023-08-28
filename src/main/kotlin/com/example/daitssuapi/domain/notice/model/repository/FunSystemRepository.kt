package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FunSystemRepository :JpaRepository<FunSystem, Long> {
}