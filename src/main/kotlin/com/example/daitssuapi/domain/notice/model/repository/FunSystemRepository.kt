package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface FunSystemRepository :JpaRepository<FunSystem, Long> {

    fun findByCategory(category: FunSystemCategory,pageable: Pageable):
            Page<FunSystem>
    fun findByCategoryAndTitleContaining(category: FunSystemCategory, title :String, pageable: Pageable):
            Page<FunSystem>
    fun findByTitleContaining(searchKeyword: String, pageable: Pageable):
            Page<FunSystem>

}