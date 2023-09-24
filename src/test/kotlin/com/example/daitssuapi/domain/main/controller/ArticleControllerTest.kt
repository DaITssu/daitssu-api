package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.domain.main.dto.request.ArticleWriteRequest
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
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
    private val userRepository: UserRepository
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
    @DisplayName("article post 성공")
    fun article_post_controller_success() {
        // given
        val baseUri = "/community/article"
        val user = userRepository.findAll().filter { null != it.nickname }[0]
        val articleWriteRequest = ArticleWriteRequest(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = user.nickname
        )

        val json = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        // when & then
        mockMvc.perform(
            post(baseUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.title").value(articleWriteRequest.title))
            .andExpect(jsonPath("$.data.content").value(articleWriteRequest.content))
            .andExpect(jsonPath("$.data.writerNickName").value(user.nickname))
            .andExpect(jsonPath("$.data.topic").value(articleWriteRequest.topic.value))
    }

    @Test
    @DisplayName("article post nickname null")
    fun article_post_nickname_null() {
        // given
        val baseUri = "/community/article"
        val articleWriteRequest = ArticleWriteRequest(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            // nickname = null
        )

        val json = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        // when & then
        mockMvc.perform(
            post(baseUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.code").value(ErrorCode.NICKNAME_REQUIRED.code))
            .andExpect(jsonPath("$.message").value(ErrorCode.NICKNAME_REQUIRED.message))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_댓글이 작성된다")
    fun writeComment() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "댓글 추가",
            userId = user.id,
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        mockMvc.perform(
            post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("실패_게시글이 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoArticle() {
        val user = userRepository.findAll()[0]
        val url = "/community/article/0/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "댓글 추가",
            userId = user.id,
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        mockMvc.perform(
            post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
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
            userId = 0,
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        mockMvc.perform(
            post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
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
            userId = user.id,
            originalCommentId = null
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        mockMvc.perform(
            post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
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
            userId = user.id,
            originalCommentId = 0
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        mockMvc.perform(
            post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.COMMENT_NOT_FOUND.code))
    }

    @Test
    @DisplayName("실패_원댓글이 다른 게시글일 때_댓글 작성에 실패한다")
    fun writeCommentFailDifferentArticle() {
        val article = articleRepository.findAll()[0]
        val originalComment = commentRepository.findAll().filter {
            it.article.id != article.id
        }[0]
        val user = userRepository.findAll()[0]
        val url = "/community/article/${article.id}/comments"
        val articleWriteRequest = CommentWriteRequest(
            content = "대충 댓글 내용",
            userId = user.id,
            originalCommentId = originalComment.id
        )
        val request = jacksonObjectMapper().writeValueAsString(articleWriteRequest)

        mockMvc.perform(
            post(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.DIFFERENT_ARTICLE.code))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_댓글들이 조회된다")
    fun getComment() {
        val articleId = commentRepository.findAll()[0].article.id
        val url = "/community/article/${articleId}/comments"

        mockMvc.perform(get(url))
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("실패_게시글을 못 찾을 때_댓글 조회에 실패한다")
    fun getCommentFailNoArticle() {
        val url = "/community/article/0/comments"

        mockMvc.perform(get(url))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.code))
    }
}
