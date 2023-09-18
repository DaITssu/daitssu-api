package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.domain.main.dto.request.ArticleWriteRequest
import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
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
    private val userRepository: UserRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("article get controller test")
    fun article_get_controller_test() {
        // given
        val baseUri = "/community/article"
        val user = userRepository.findAll().filter { null != it.nickname }[0]
        val savedArticle = articleRepository.save(Article(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            writer = user
        ))

        // when & then
        mockMvc.perform(
            get("$baseUri/${savedArticle.id}")
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.title").value(savedArticle.title))
            .andExpect(jsonPath("$.data.content").value(savedArticle.content))
            .andExpect(jsonPath("$.data.writerNickName").value(user.nickname))
            .andExpect(jsonPath("$.data.topic").value(savedArticle.topic.value))
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
}
