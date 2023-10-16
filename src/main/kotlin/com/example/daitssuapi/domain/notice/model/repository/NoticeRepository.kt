package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page

@Repository
interface NoticeRepository :JpaRepository<Notice, Long>{
    fun findByCategory(
        category : NoticeCategory,
        pageable: Pageable,
    ): Page<Notice>
}