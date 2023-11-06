package com.example.daitssuapi.domain.mypage.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.mypage.dto.response.MyArticleResponse
import com.example.daitssuapi.domain.mypage.service.MypageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/mypage")
class MypageController(
    private val mypageService: MypageService
) {
    
    @GetMapping("/{userId}/articles")
    fun getMyArticle(
        @PathVariable("userId") userId: Long
    ) : Response<List<MyArticleResponse>> =
        Response(data = mypageService.getMyArticle(userId = userId))

}