package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface FunSystemRepository :JpaRepository<FunSystem, Long> {

    fun findByCategory(category: FunSystemCategory):
            List<FunSystem>
    fun findByCategoryAndTitleContaining(category: FunSystemCategory, title :String):
            List<FunSystem>
    fun findByTitleContaining(searchKeyword: String):
            List<FunSystem>
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="UPDATE FunSystem SET views=views+1 WHERE id= :id")
    fun updateViewsById(@Param("id") id: Long)
}