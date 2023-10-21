package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@IntegrationTest
class MyPageServiceTest(
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
            comments = comments.filter { it.writer.id == userId }.map { it.id }
        )

        myPageService.deleteComments(comments = request.comments)

        comments.filter { it.writer.id == userId }.forEach { comment ->
            assertThat(comment.isDeleted).isTrue()
        }
    }
}
