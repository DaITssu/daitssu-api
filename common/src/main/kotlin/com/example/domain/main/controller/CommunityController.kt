package com.example.domain.main.controller

import com.example.common.dto.Response
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.domain.main.service.CommunityService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/daitssu/community")
class CommunityController(
    private val communityService: CommunityService
) {
    @GetMapping("/{id}")
    fun getArticle(@PathVariable id: Long): Response<ArticleResponse>
        = Response(data = communityService.getArticle(id))

    @PostMapping("/write")
    fun writeArticle(@RequestBody articlePostRequest: ArticlePostRequest): Response<ArticleResponse>
        = Response(data = communityService.writeArticle(articlePostRequest))
}