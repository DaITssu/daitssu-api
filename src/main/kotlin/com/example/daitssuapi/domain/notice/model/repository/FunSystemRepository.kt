package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.domain.notice.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FunSystemRepository :JpaRepository<FunSystem, Long> {

    fun findByCategory(category: FunSystemCategory):
            List<FunSystem>
    fun findByCategoryAndTitleContaining(category: FunSystemCategory, title :String):
            List<FunSystem>
    fun findByTitleContaining(searchKeyword: String):
            List<FunSystem>

}