package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.domain.main.dto.request.ArticleWriteRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.DepartmentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

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
        val articleWriteRequest = ArticleWriteRequest(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = user.nickname!!
        )
        val articleResponse = articleService.writeArticle(articleWriteRequest)

        // then
        assertEquals(articleWriteRequest.topic.value, articleResponse.topic)
        assertEquals(articleWriteRequest.title, articleResponse.title)
        assertEquals(articleWriteRequest.content, articleResponse.content)
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
        val articleResponse: ArticleResponse = articleService.getArticle(savedArticle.id)

        // then
        assertEquals(articleResponse.id, savedArticle.id)
        assertEquals(articleResponse.topic, savedArticle.topic.value)
        assertEquals(articleResponse.title, savedArticle.title)
        assertEquals(articleResponse.content, savedArticle.content)
        assertEquals(articleResponse.writerNickName, savedArticle.writer.nickname)
    }
}