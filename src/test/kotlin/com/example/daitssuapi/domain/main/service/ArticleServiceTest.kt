package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.domain.main.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@IntegrationTest
class ArticleServiceTest(
    private val articleService: ArticleService,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) {
    @Test
    @DisplayName("article 생성 테스트")
    fun article_generation_test() {
        // given
        val user = userRepository.findAll().filter { null != it.nickname }[0]

        // when
        val articleCreateRequest = ArticleCreateRequest(
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            nickname = user.nickname!!
        )
        val articleResponse = articleService.createArticle(articleCreateRequest)

        // then
        assertEquals(articleCreateRequest.topic.value, articleResponse.topic)
        assertEquals(articleCreateRequest.title, articleResponse.title)
        assertEquals(articleCreateRequest.content, articleResponse.content)
        assertEquals(user.nickname, articleResponse.writerNickName)
    }

    @Test
    @DisplayName("article 조회 테스트")
    fun article_find_test() {
        // given
        val user = userRepository.findAll().filter { null != it.nickname }[0]
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
