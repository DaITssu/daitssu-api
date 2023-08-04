package com.example.domain.main.service

import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
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
class CommunityServiceTest(
    private val communityService: CommunityService,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val departmentRepository: DepartmentRepository
) {
    @BeforeEach
    fun setUser() {
        val department = Department(name = "xx학부")
        departmentRepository.save(department)

        val user = User(studentId = 20221111, name = "홍길동",
            department = department)
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
        val user = userRepository.findByStudentId(20221111)
            ?: throw Error()

        // when
        val articlePostRequest = ArticlePostRequest(title = "테스트 제목",
            content = "테스트 내용", studentId = user.studentId)
        val articleResponse = communityService.writeArticle(articlePostRequest)

        // then
        assertEquals(articlePostRequest.title, articleResponse.title)
        assertEquals(articlePostRequest.content, articleResponse.content)
        assertEquals(user.id, articleResponse.writer.id)
    }

    @Test
    @DisplayName("article 조회 테스트")
    fun article_find_test() {
        // given
        val user: User = userRepository.findByStudentId(20221111) ?: throw Error()
        val article = Article(title = "테스트 제목", content = "테스트 내용", writer = user)
        val savedArticle = articleRepository.save(article)

        // when
        val selectedArticle: ArticleResponse = communityService.getArticle(savedArticle.id)

        // then
        assertEquals(selectedArticle.id, savedArticle.id)
        assertEquals(selectedArticle.title, savedArticle.title)
        assertEquals(selectedArticle.content, savedArticle.content)
        assertEquals(selectedArticle.writer.id, savedArticle.writer.id)
    }
}