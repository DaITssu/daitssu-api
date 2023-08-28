package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.model.entity.Notice
import com.example.daitssuapi.domain.notice.model.repository.FunSystemRepository
import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NoticeService (
    private val noticeRepository: NoticeRepository,
    private val funSystemRepository : FunSystemRepository,
){
    fun getNoticeList(
        category: String
    ):List<NoticeResponse> {
        if(category == "전체"){
            val notices :List<Notice> = noticeRepository.findAll()
            return notices.map { NoticeResponse.fromNotice(it) }
        }else{
            val notices :List<Notice> = noticeRepository.findByCategory(category)
            return notices.map { NoticeResponse.fromNotice(it) }
        }


    }
    fun getNoticePage(
        id: Long
    ):NoticeResponse{
        val notice :Notice = noticeRepository.findById(id).get() // 예외처리 필요
        return NoticeResponse.fromNotice(notice)
    }

}


