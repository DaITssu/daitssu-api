package com.example.daitssuapi.domain.myPage.service

import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.article.model.repository.ArticleRepository
import com.example.daitssuapi.domain.article.model.repository.CommentRepository
import com.example.daitssuapi.domain.myPage.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

@IntegrationTest
class MyPageServiceTest(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val myPageService: MyPageService
) {
    @Test
    @DisplayName("성공_자신의 userId를 넘겨줄 때_자신의 댓글이 조회된다")
    fun successGetComments() {
        val comments = commentRepository.findAll()
        val userId = comments[0].writer.id

        val findComments = myPageService.getComments(userId = userId)

        assertThat(findComments.size).isEqualTo(comments.filter { it.writer.id == userId }.filter { !it.isDeleted }.size)
    }

    @Test
    @DisplayName("성공_자신의 userId를 넘겨줄 때_댓글이 없다면 조회되는 댓글이 없다")
    fun successGetCommentsNoComments() {
        val userIds = commentRepository.findAll().map { it.writer.id }
        val userId = userRepository.findAll().filter { !userIds.contains(it.id) }[0].id

        val findComments = myPageService.getComments(userId = userId)

        assertThat(findComments.size).isZero
    }

    @Test
    @DisplayName("실패_올바르지 않은 userId를 넘겨주면_댓글 조회에 실패한다")
    fun failGetCommentsUserNotExist() {
        val userId = 0L

        assertThrows<DefaultException> { myPageService.getComments(userId = userId) }
    }

    @Test
    @DisplayName("성공_작성한 댓글을 넘겨줄 때_비활성화 상태가 된다")
    fun successDeleteComments() {
        val comments = commentRepository.findAll()
        val userId = comments[0].writer.id
        val request = CommentDeleteRequest(
            commentIds = comments.filter { it.writer.id == userId }.map { it.id }
        )

        myPageService.deleteComments(commentIds = request.commentIds)

        comments.filter { it.writer.id == userId }.forEach { comment ->
            assertThat(comment.isDeleted).isTrue()
        }
    }

    @Test
    @DisplayName("올바른 userId를 받으면 작성한 게시글이 조회된다")
    fun get_my_articles_with_user_id() {
        val userId = 4L
        val user = userRepository.findById(userId).get()
        val articles = articleRepository.findAllByWriterOrderByCreatedAtDesc(user)
        val findArticles = myPageService.getMyArticle(userId = userId)

        assertAll(
            { assertThat(findArticles).isNotEmpty },
            { assertThat(findArticles.size).isEqualTo(articles.size) }
        )
    }

    @Test
    @DisplayName("없는 userId를 받으면 USER_NOT_FOUND 에러가 발생한다")
    fun get_my_articles_with_wrong_user_id() {
        val wrongUserId = 999L

        assertThrows<DefaultException> {
            myPageService.getMyArticle(userId = wrongUserId)
        }
    }
}
