package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.domain.notice.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository :JpaRepository<Notice, Long>{

    fun findByCategoryAndTitleContaining(category:NoticeCategory,
                                         title :String,
                                         pageable: Pageable): Page<Notice>
    fun findByTitleContaining(searchKeyword: String,pageable: Pageable): Page<Notice>

    fun findByCategory(
            category : NoticeCategory,
            pageable: Pageable,
    ): Page<Notice>
}