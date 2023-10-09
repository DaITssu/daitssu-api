package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

@IntegrationTest
class ArticleServiceTest(
    private val articleService: ArticleService,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository
) {
    @Test
    @DisplayName("article 생성 테스트")
    fun article_generation_test() {
        // given
        val user = userRepository.findAll().filter { null != it.nickname }[0]

        // when
        val articleCreateRequest = ArticleCreateRequest(
            userId = user.id,
            topic = Topic.CHAT,
            title = "테스트 제목",
            content = "테스트 내용",
            images = emptyList()
        )

        articleService.createArticle(articleCreateRequest)
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

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_댓글이 작성된다")
    fun writeComment() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val request = CommentWriteRequest(
            content = "댓글 추가",
            userId = user.id,
            originalCommentId = null
        )

        val response = articleService.writeComment(articleId = article.id, request = request)

        assertAll(
            { assertThat(response.userId).isEqualTo(user.id) },
            { assertThat(response.content).isEqualTo(request.content) },
            { assertThat(response.originalCommentId).isEqualTo(request.originalCommentId) }
        )
    }

    @Test
    @DisplayName("실패_게시글이 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoArticle() {
        val user = userRepository.findAll()[0]
        val request = CommentWriteRequest(
            content = "댓글 추가",
            userId = user.id,
            originalCommentId = null
        )

        assertThrows<DefaultException> { articleService.writeComment(articleId = 0, request = request) }
    }

    @Test
    @DisplayName("실패_유저가 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoUser() {
        val article = articleRepository.findAll()[0]
        val request = CommentWriteRequest(
            content = "댓글 추가",
            userId = 0,
            originalCommentId = null
        )

        assertThrows<DefaultException> { articleService.writeComment(articleId = article.id, request = request) }
    }

    @Test
    @DisplayName("실패_댓글이 너무 길 때_댓글 작성에 실패한다")
    fun writeCommentFailTooLongComment() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val request = CommentWriteRequest(
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

        assertThrows<DefaultException> { articleService.writeComment(articleId = article.id, request = request) }
    }

    @Test
    @DisplayName("실패_원댓글이 없을 때_댓글 작성에 실패한다")
    fun writeCommentFailNoOriginalArticle() {
        val article = articleRepository.findAll()[0]
        val user = userRepository.findAll()[0]
        val request = CommentWriteRequest(
            content = "대충 댓글 내용",
            userId = user.id,
            originalCommentId = 0
        )

        assertThrows<DefaultException> { articleService.writeComment(articleId = article.id, request = request) }
    }

    @Test
    @DisplayName("실패_원댓글이 다른 게시글일 때_댓글 작성에 실패한다")
    fun writeCommentFailDifferentArticle() {
        val article = articleRepository.findAll()[0]
        val originalComment = commentRepository.findAll().filter {
            it.article?.id != article.id
        }[0]
        val user = userRepository.findAll()[0]
        val request = CommentWriteRequest(
            content = "대충 댓글 내용",
            userId = user.id,
            originalCommentId = originalComment.id
        )

        assertThrows<DefaultException> { articleService.writeComment(articleId = article.id, request = request) }
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 때_댓글들이 조회된다")
    fun getComment() {
        val comment = commentRepository.findAll().filter { null != it.article }[0]

        val response = articleService.getComments(articleId = comment.article!!.id)

        assertAll(
            { assertThat(response.size).isNotEqualTo(0) }
        )
    }

    @Test
    @DisplayName("실패_게시글을 못 찾을 때_댓글 조회에 실패한다")
    fun getCommentFailNoArticle() {
        assertThrows<DefaultException> { articleService.getComments(articleId = 0) }
    }
}
