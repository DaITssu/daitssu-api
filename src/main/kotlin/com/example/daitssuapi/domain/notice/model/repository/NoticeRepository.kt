package com.example.daitssuapi.domain.notice.model.repository

import com.example.daitssuapi.domain.notice.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.model.entity.Notice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository :JpaRepository<Notice, Long>{
    fun findByCategory(category: NoticeCategory):
            List<Notice>
    fun findByCategoryAndTitleContaining(category: NoticeCategory, title :String):
            List<Notice>
    fun findByTitleContaining(searchKeyword: String):
            List<Notice>

}