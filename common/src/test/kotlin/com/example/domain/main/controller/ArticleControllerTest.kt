package com.example.domain.main.controller

import com.example.common.dto.Response
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.domain.main.enums.Topic
import com.example.domain.main.model.entity.Article
import com.example.domain.main.model.entity.Department
import com.example.domain.main.model.entity.User
import com.example.domain.main.model.repository.ArticleRepository
import com.example.domain.main.model.repository.DepartmentRepository
import com.example.domain.main.model.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.assertEquals

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ArticleControllerTest(
    private val articleController: ArticleController,
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {
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
        val user = userRepository.findAll()[0]
        val article: Article = Article(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            writer = user
        )
        val savedArticle = articleRepository.save(article)

        // when
        val articleResponse: Response<ArticleResponse> = articleController.getArticle(savedArticle.id)

        // then
        assertEquals(savedArticle.id, articleResponse.data?.id)
        assertEquals(savedArticle.topic.name, articleResponse.data?.topic)
        assertEquals(savedArticle.title, articleResponse.data?.title)
        assertEquals(savedArticle.content, articleResponse.data?.content)
        assertEquals(savedArticle.writer.nickname, articleResponse.data?.writerNickName)
    }

    @Test
    @DisplayName("article post controller test")
    fun article_post_controller_test() {
        // given
        val user = userRepository.findAll()[0]
        val articlePostRequest = ArticlePostRequest(
            topic = Topic.CHAT.name,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = user.nickname!!
        )

        // when
        val articleResponse: Response<ArticleResponse> = articleController.writeArticle(articlePostRequest)

        // then
        assertEquals(articlePostRequest.topic, articleResponse.data?.topic)
        assertEquals(articlePostRequest.title, articleResponse.data?.title)
        assertEquals(articlePostRequest.content, articleResponse.data?.content)
        assertEquals(articlePostRequest.nickname, articleResponse.data?.writerNickName)
    }
}