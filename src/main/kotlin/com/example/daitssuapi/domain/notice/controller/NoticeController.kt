package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.springframework.test.context.TestPropertySource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@TestPropertySource("classpath:/application-local.yml")
@RestController
@RequestMapping("/notice")
class NoticeController (
    private val noticeService : NoticeService,
){

    @GetMapping("/{category}")
    fun getNoticeList(
        @PathVariable category: String
    ): Response<List<NoticeResponse>> =
        Response(data = noticeService.getNoticeList(category))

    @GetMapping("/{id}")
    fun getNoticePage(
        @PathVariable id: Long,
    ): Response<NoticeResponse> =
        Response(data = noticeService.getNoticePage(id))
}


