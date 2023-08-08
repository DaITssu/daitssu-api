package com.example.domain.main.service

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
class ArticleServiceTest(
    private val articleService: ArticleService,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
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
    @DisplayName("article 생성 테스트")
    fun article_generation_test() {
        // given
        val user = userRepository.findAll()[0]

        // when
        val articlePostRequest = ArticlePostRequest(
            topic = Topic.CHAT.name,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = user.nickname!!
        )
        val articleResponse = articleService.writeArticle(articlePostRequest)

        // then
        assertEquals(articlePostRequest.title, articleResponse.title)
        assertEquals(articlePostRequest.content, articleResponse.content)
        assertEquals(user.nickname, articleResponse.writerNickName)
    }

    @Test
    @DisplayName("article 조회 테스트")
    fun article_find_test() {
        // given
        val user = userRepository.findAll()[0]
        val article = Article(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            writer = user
        )
        val savedArticle = articleRepository.save(article)

        // when
        val selectedArticle: ArticleResponse = articleService.getArticle(savedArticle.id)

        // then
        assertEquals(selectedArticle.id, savedArticle.id)
        assertEquals(selectedArticle.title, savedArticle.title)
        assertEquals(selectedArticle.content, savedArticle.content)
        assertEquals(selectedArticle.writerNickName, savedArticle.writer.nickname)
    }
}