package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.domain.main.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.ControllerTest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ControllerTest
class MyPageControllerTest(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val baseUrl = "/mypage"

    @Test
    @DisplayName("성공_자신의 userId를 넘겨줄 때_자신의 댓글이 조회된다")
    fun successGetComments() {
        val userId = commentRepository.findAll()[0].writer.id
        val url = "$baseUrl/$userId/comments"

        mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @DisplayName("성공_자신의 userId를 넘겨줄 때_댓글이 없다면 조회되는 댓글이 없다")
    fun successGetCommentsNoComments() {
        val userIds = commentRepository.findAll().map { it.writer.id }
        val userId = userRepository.findAll().filter { !userIds.contains(it.id) }[0].id
        val url = "$baseUrl/$userId/comments"

        mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @DisplayName("실패_올바르지 않은 userId를 넘겨주면_댓글 조회에 실패한다")
    fun failGetCommentsUserNotExist() {
        val userId = 0
        val url = "$baseUrl/$userId/comments"

        mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.code))
    }

    @Test
    @DisplayName("성공_작성한 댓글을 넘겨줄 때_비활성화 상태가 된다")
    fun successDeleteComments() {
        val comments = commentRepository.findAll()
        val userId = comments[0].writer.id
        val commentDeleteRequest = CommentDeleteRequest(
            comments = comments.filter { it.writer.id == userId }.filter { !it.isDeleted }.map { it.id }
        )
        val url = "$baseUrl/$userId/comments"
        val request = jacksonObjectMapper().writeValueAsString(commentDeleteRequest)

        mockMvc.perform(
            MockMvcRequestBuilders.patch(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)

        comments.filter { it.writer.id == userId }.forEach { comment ->
            assertThat(comment.isDeleted).isTrue()
        }
    }
}
