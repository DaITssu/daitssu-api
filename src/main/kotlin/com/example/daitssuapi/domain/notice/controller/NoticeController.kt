package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/notice")
class NoticeController (
    private val noticeService : NoticeService,
){

    @GetMapping("/{category}")
    fun getNoticeList(
        @PathVariable category: String,
        @RequestParam(required = false) searchKeyword : String?
    ): Response<List<NoticeResponse>>{
        if(searchKeyword == null)
            return Response(data = noticeService.getNoticeList(category))
        else
            return Response(data = noticeService.getNoticeSearchList(category,searchKeyword))
    }

    @GetMapping("/page/{id}")
    fun getNoticePage(
        @PathVariable id: Long,
    ): Response<NoticePageResponse> {
        noticeService.updateViews(id)
        return Response(data = noticeService.getNoticePage(id))
    }

}


