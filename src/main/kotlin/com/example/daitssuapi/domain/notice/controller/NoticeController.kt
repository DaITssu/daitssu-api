package com.example.daitssuapi.domain.notice.controller
import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.web.PageableDefault
import kotlin.math.max
import kotlin.math.min

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

    @GetMapping("/page/{id}")
    fun getNoticePage(
        @PathVariable id: Long,
        @PageableDefault(page = 0, size = 1, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,

        nowPage: Int = pageable.pageNumber+1,
        startPage: Int = max(nowPage -4, 1),
        endPage: Int = min(nowPage +5,   2147483647),

        ): Response<NoticeResponse> =
        Response(data = noticeService.getNoticePage(id, pageable, nowPage, startPage, endPage))
}



