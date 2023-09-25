package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface NoticeRepository :JpaRepository<Notice, Long>{
    fun findByCategory(category:NoticeCategory):
            List<Notice>
    fun findByCategoryAndTitleContaining(category:NoticeCategory,title :String):
            List<Notice>
    fun findByTitleContaining(searchKeyword: String):
            List<Notice>
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="UPDATE Notice SET views=views+1 WHERE id= :id")
    fun updateViewsById(@Param("id") id: Long)

}