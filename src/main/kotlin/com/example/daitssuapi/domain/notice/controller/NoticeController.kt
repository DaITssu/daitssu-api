package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.common.enums.NoticeCategory
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.dto.NoticePageResponse
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/notice")
class NoticeController (
    private val noticeService : NoticeService,
){
    @GetMapping
    fun getAllNoticeList(
        @RequestParam searchKeyword:String? = null
    ): Response<List<NoticeResponse>>{
        return Response(data = noticeService.getAllNoticeList(searchKeyword))
    }

    @GetMapping("/{category}")
    fun getNoticeListWithCategory(
        @PathVariable category: NoticeCategory,
        @RequestParam searchKeyword:String? = null,
    ): Response<List<NoticeResponse>>{
        return Response(data = noticeService.getNoticeList(category, searchKeyword))
    }

    @GetMapping("/page/{id}")
    fun getNoticePage(
        @PathVariable id: Long,
    ): Response<NoticePageResponse> {
        noticeService.updateViews(id)
        return Response(data = noticeService.getNoticePage(id))
    }

}


