package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository :JpaRepository<Notice, Long>{
    fun findByCategory(category:NoticeCategory): List<Notice>
    fun findByCategoryAndTitleContaining(category:NoticeCategory,title :String): List<Notice>
    fun findByTitleContaining(searchKeyword: String): List<Notice>

    fun findByCategory(
            category : NoticeCategory,
            pageable: Pageable,
    ): Page<Notice>
}