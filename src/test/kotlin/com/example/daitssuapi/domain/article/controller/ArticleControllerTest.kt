package com.example.daitssuapi.domain.article.controller

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.security.component.TokenProvider
import com.example.daitssuapi.domain.article.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.article.model.repository.ArticleRepository
import com.example.daitssuapi.domain.article.model.repository.CommentRepository
import com.example.daitssuapi.domain.article.model.repository.ScrapRepository
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import com.example.daitssuapi.utils.ControllerTest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class ArticleControllerTest(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val scrapRepository: ScrapRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("article get controller test")
    fun article_get_controller_test() {
        // given
        val baseUri = "/community/article"
        val article = articleRepository.findAll()[0]

        // when & then
        mockMvc.perform(
            get("$baseUri/${article.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.title").value(article.title))
            .andExpect(jsonPath("$.data.content").value(article.content))
            .andExpect(jsonPath("$.data.writerNickName").value(article.writer.nickname))
            .andExpect(jsonPath("$.data.topic").value(article.topic.value))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_댓글이 작성된다")
    fun writeComment() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "댓글 추가",
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)
        val accessToken = tokenProvider.createAccessToken(id = user.id).token

        mockMvc.perform(
            post(url).content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("실패_게시글이 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoArticle() {
        val user = userRepository.findAll()[0]
        val url = "/community/article/0/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "댓글 추가",
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)
        val accessToken = tokenProvider.createAccessToken(id = user.id).token

        mockMvc.perform(post(url)
            .content(request)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.code))
    }

    @Test
    @DisplayName("실패_유저가 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoUser() {
        val article = articleRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "댓글 추가",
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)
        val accessToken = tokenProvider.createAccessToken(id = 0).token

        mockMvc.perform(
            post(url).content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.code))
    }

    @Test
    @DisplayName("실패_댓글이 너무 길 때_댓글 작성에 실패한다")
    fun writeCommentFailTooLongComment() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = """
                512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복
                512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복
                512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복
                512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복
                512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복
                512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복512Byte 넘기기 위해 대충 반복
            """.trimIndent(),
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)
        val accessToken = tokenProvider.createAccessToken(id = user.id).token

        mockMvc.perform(post(url)
            .content(request)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.COMMENT_TOO_LONG.code))
    }

    @Test
    @DisplayName("실패_원댓글이 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoOriginalArticle() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "대충 댓글 내용",
            originalCommentId = 0
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)
        val accessToken = tokenProvider.createAccessToken(id = user.id).token

        mockMvc.perform(post(url)
            .content(request)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.COMMENT_NOT_FOUND.code))
    }

    @Test
    @DisplayName("실패_원댓글이 다른 게시글일 때_댓글 작성에 실패한다")
    fun writeCommentFailDifferentArticle() {
        val article = articleRepository.findAll()[0]
        val originalComment = commentRepository.findAll().filter {
            it.article?.id != article.id
        }[0]
        val user = userRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "대충 댓글 내용",
            originalCommentId = originalComment.id
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)
        val accessToken = tokenProvider.createAccessToken(id = user.id).token

        mockMvc.perform(post(url)
            .content(request)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.DIFFERENT_ARTICLE.code))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_댓글들이 조회된다")
    fun getComment() {
        val articleId = commentRepository.findAll().filter { null != it.article }[0].article!!.id
        val url = "/community/article/${articleId}/comments"

        mockMvc.perform(get(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("실패_게시글을 못 찾을 때_댓글 조회에 실패한다")
    fun getCommentFailNoArticle() {
        val url = "/community/article/0/comments"

        mockMvc.perform(get(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.code))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_새로운 게시글을 스크랩한다")
    fun newScrapSuccess() {
        val userId = userRepository.findAll().sortedByDescending { it.id }[0].id
        val articleId = commentRepository.findAll().filter { null != it.article }[0].id
        val url = "/community/article/${articleId}/scrap"
        val accessToken = tokenProvider.createAccessToken(id = userId).token

        mockMvc.perform(post(url)
            .param("userId", userId.toString())
            .param("isActive", true.toString())
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("실패_신규 스크랩에서 isActive가 false라면_스크랩에 실패한다")
    fun newScrapFailIsActiveFalse() {
        val userId = userRepository.findAll().sortedByDescending { it.id }[0].id
        val articleId = commentRepository.findAll().filter { null != it.article }[0].id
        val url = "/community/article/${articleId}/scrap"
        val accessToken = tokenProvider.createAccessToken(id = userId).token

        mockMvc.perform(post(url)
            .param("userId", userId.toString())
            .param("isActive", false.toString())
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("실패_신규 스크랩에서 유저를 못 찾으면_스크랩에 실패한다")
    fun newScrapFailNoUser() {
        val userId = 0L
        val articleId = commentRepository.findAll().filter { null != it.article }[0].id
        val url = "/community/article/${articleId}/scrap"
        val accessToken = tokenProvider.createAccessToken(id = userId).token

        mockMvc.perform(post(url)
            .param("userId", userId.toString())
            .param("isActive", false.toString())
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("실패_신규 스크랩에서 게시글을 못 찾으면_스크랩에 실패한다")
    fun newScrapFailNoArticle() {
        val userId = userRepository.findAll()[0].id
        val articleId = 0
        val url = "/community/article/${articleId}/scrap"
        val accessToken = tokenProvider.createAccessToken(id = userId).token

        mockMvc.perform(post(url)
            .param("userId", userId.toString())
            .param("isActive", false.toString())
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_기존 스크랩의 상태를 변경한다")
    fun editScrapSuccess() {
        val scrap = scrapRepository.findAll()[0]
        val url = "/community/article/${scrap.article.id}/scrap"
        val accessToken = tokenProvider.createAccessToken(id = scrap.user.id).token

        mockMvc.perform(post(url)
            .param("isActive", (!scrap.isActive).toString())
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
    }
}
