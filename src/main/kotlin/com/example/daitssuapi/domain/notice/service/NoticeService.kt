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
import org.springframework.transaction.annotation.Transactional

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
    @Transactional
    fun updateViews( id:Long ) {
        val notice =noticeRepository.findByIdOrNull(id)
            ?:throw DefaultException(ErrorCode.NOTICE_NOT_FOUND)
        notice.views = notice.views +1
        noticeRepository.save(notice)
    }

}



