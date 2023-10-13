package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.model.entity.Notice
import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NoticeService (
    private val noticeRepository: NoticeRepository,
){
    fun getAllNoticeList(searchKeyword:String?):List<NoticeResponse>{
        val notices: List<Notice>
        if(searchKeyword==null){
            notices = noticeRepository.findAll()
        }else{
            notices= noticeRepository.findByTitleContaining(searchKeyword)
        }
        return notices.map { NoticeResponse.fromNotice(it) }
    }

    fun getNoticeList(
        category: NoticeCategory,
        searchKeyword: String?,
    ):List<NoticeResponse>{
        val notices : List<Notice>
        if(searchKeyword==null){
            notices = noticeRepository.findByCategory(category)
        }else{
            notices = noticeRepository.findByCategoryAndTitleContaining(category,searchKeyword)
        }
        return notices.map{ NoticeResponse.fromNotice(it)}
    }

    fun getNoticePage(
        id: Long
    ): NoticePageResponse {
        val notice :Notice = noticeRepository.findByIdOrNull(id)
            ?: throw DefaultException(errorCode= ErrorCode.NOTICE_NOT_FOUND)

        return NoticePageResponse.fromNotice(notice)
    }

    fun updateViews( id:Long ) { noticeRepository.updateViewsById(id)}

}



