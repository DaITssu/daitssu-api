package com.example.daitssuapi.domain.myPage.controller

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.objectMapper
import com.example.daitssuapi.common.security.component.TokenProvider
import com.example.daitssuapi.domain.article.model.repository.CommentRepository
import com.example.daitssuapi.domain.myPage.dto.request.CommentDeleteRequest
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import com.example.daitssuapi.utils.ControllerTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class MyPageControllerTest(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val baseUrl = "/myPage"

    @Test
    @DisplayName("성공_자신의 userId를 넘겨줄 때_자신의 댓글이 조회된다")
    fun successGetComments() {
        val userId = commentRepository.findAll()[0].writer.id
        val url = "$baseUrl/comments"
        val accessToken = tokenProvider.createAccessToken(id = userId).token

        mockMvc.perform(get(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("성공_자신의 userId를 넘겨줄 때_댓글이 없다면 조회되는 댓글이 없다")
    fun successGetCommentsNoComments() {
        val userIds = commentRepository.findAll().map { it.writer.id }
        val userId = userRepository.findAll().filter { !userIds.contains(it.id) }[0].id
        val url = "$baseUrl/comments"
        val accessToken = tokenProvider.createAccessToken(id = userId).token

        mockMvc.perform(get(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("실패_올바르지 않은 userId를 넘겨주면_댓글 조회에 실패한다")
    fun failGetCommentsUserNotExist() {
        val url = "$baseUrl/comments"
        val accessToken = tokenProvider.createAccessToken(id = 0).token

        mockMvc.perform(get(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.code))
    }

    @Test
    @DisplayName("성공_작성한 댓글을 넘겨줄 때_비활성화 상태가 된다")
    fun successDeleteComments() {
        val comments = commentRepository.findAll()
        val userId = comments[0].writer.id
        val commentDeleteRequest = CommentDeleteRequest(
            commentIds = comments.filter { it.writer.id == userId }.filter { !it.isDeleted }.map { it.id }
        )
        val url = "$baseUrl/comments"
        val request = objectMapper.writeValueAsString(commentDeleteRequest)
        val accessToken = tokenProvider.createAccessToken(id = 0).token

        mockMvc.perform(patch(url)
            .content(request)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)

        comments.filter { it.writer.id == userId }.forEach { comment ->
            Assertions.assertThat(comment.isDeleted).isTrue()
        }
    }

    @Test
    @DisplayName("게시글이 있는 userId를 이용하여 게시글 조회시 _ 1개 이상의 게시글이 조회된다")
    fun get_my_articles_with_user_id() {
        val accessToken = tokenProvider.createAccessToken(id = 4L).token

        mockMvc.perform(get("$baseUrl/articles")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("게시글이 없는 userId를 이용하여 게시글 조회시 _ 빈 List를 받는다")
    fun get_my_articles_with_wrong_user_id() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token

        mockMvc.perform(get("$baseUrl/articles")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @DisplayName("스크랩한 userId를 이용하여 게시글 조회시 _ 1개 이상의 게시글이 조회된다")
    fun get_my_scraps_with_user_id() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token

        mockMvc.perform(get("$baseUrl/scraps")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("스크랩하지 않은 userId를 이용하여 게시글 조회시 _ 빈 리스트가 출력된다")
    fun get_my_scraps_with_wrong_user_id() {
        val accessToken = tokenProvider.createAccessToken(id = 3L).token

        mockMvc.perform(get("$baseUrl/scraps")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @DisplayName("성공_올바른 userId를 넘겨주면_과제를 조회한다")
    fun successGetAssignments() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token

        mockMvc.perform(get("$baseUrl/assignments")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("성공_올바른 userId와 courseId를 넘겨주면_과제를 조회한다")
    fun successGetAssignmentsWithCourseId() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token
        val courseId = 1L

        mockMvc.perform(get("$baseUrl/assignments")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .param("courseId", courseId.toString())
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("실패_올바르지 않은 userId 넘겨주면_과제 조회에 실패한다")
    fun failGetAssignments() {
        val accessToken = tokenProvider.createAccessToken(id = 0L).token

        mockMvc.perform(get("$baseUrl/assignments")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("실패_올바르지 않은 courseId를 넘겨주면_과제 조회에 실패한다")
    fun failGetAssignmentsWithCourseId() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token
        val courseId = 0L

        mockMvc.perform(get("$baseUrl/assignments")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .param("courseId", courseId.toString())
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("성공_올바른 userId와 courseId를 넘겨주면_과제를 조회한다")
    fun successGetCourseNotices() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token
        val courseId = 1L

        mockMvc.perform(get("$baseUrl/course/$courseId/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .param("courseId", courseId.toString())
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("실패_올바르지 않은 userId 넘겨주면_과제 조회에 실패한다")
    fun failGetCourseNotices() {
        val accessToken = tokenProvider.createAccessToken(id = 0L).token
        val courseId = 1L

        mockMvc.perform(get("$baseUrl/course/$courseId/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .param("courseId", courseId.toString())
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("실패_올바르지 않은 courseId를 넘겨주면_과제 조회에 실패한다")
    fun failGetCourseNoticesWithCourseId() {
        val accessToken = tokenProvider.createAccessToken(id = 1L).token
        val wrongCourseId = 0L

        mockMvc.perform(get("$baseUrl/course/$wrongCourseId/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .param("courseId", wrongCourseId.toString())
        ).andExpect(status().isBadRequest)
    }

    @DisplayName("서비스 공지사항 전제 조회")
    fun get_service_notice() {

        mockMvc.perform(get("$baseUrl/service-notice"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
            .andReturn()
    }
}
