package com.example.domain.main.controller

import com.example.common.dto.Response
import com.example.common.enums.ErrorCode
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.domain.main.enums.Topic
import com.example.domain.main.model.entity.Article
import com.example.domain.main.model.entity.Department
import com.example.domain.main.model.entity.User
import com.example.domain.main.model.repository.ArticleRepository
import com.example.domain.main.model.repository.DepartmentRepository
import com.example.domain.main.model.repository.UserRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.assertEquals

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
class ArticleControllerTest(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUser() {
        val department = Department(name = "xx학부")
        departmentRepository.save(department)

        val user = User(
            studentId = 20221111,
            name = "홍길동",
            nickname = "의적",
            department = department
        )
        userRepository.save(user)
    }

    @AfterEach
    fun deleteDB() {
        articleRepository.deleteAll()
        userRepository.deleteAll()
        departmentRepository.deleteAll()
    }

    @Test
    @DisplayName("article get controller test")
    fun article_get_controller_test() {
        // given
        val baseUri = "/daitssu/community/article"
        val user = userRepository.findAll()[0]
        val article: Article = Article(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            writer = user
        )
        val savedArticle = articleRepository.save(article)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$baseUri/${article.id}")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(article.title))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(article.content))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.writerNickName").value(user.nickname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.topic").value(article.topic.value))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("article post 성공")
    fun article_post_controller_success() {
        // given
        val baseUri = "/daitssu/community/article"
        val user = userRepository.findAll()[0]
        val articlePostRequest = ArticlePostRequest(
            topic = Topic.CHAT.value,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = user.nickname
        )

        val json = jacksonObjectMapper().writeValueAsString(articlePostRequest)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content()
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.title")
                .value(articlePostRequest.title))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.content")
                .value(articlePostRequest.content))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.writerNickName")
                .value(user.nickname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.topic")
                .value(articlePostRequest.topic))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("article post nickname null")
    fun article_post_nickname_null() {
        // given
        val baseUri = "/daitssu/community/article"
        val articlePostRequest = ArticlePostRequest(
            topic = Topic.CHAT.value,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = null
        )

        val json = jacksonObjectMapper().writeValueAsString(articlePostRequest)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value(ErrorCode.BAD_REQUEST.message))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code")
                .value(ErrorCode.BAD_REQUEST.code))
            .andDo(MockMvcResultHandlers.print())
    }
}