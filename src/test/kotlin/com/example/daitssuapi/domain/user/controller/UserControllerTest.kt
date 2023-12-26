package com.example.daitssuapi.domain.user.controller

import com.example.daitssuapi.common.security.component.TokenProvider
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import com.example.daitssuapi.utils.ControllerTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class UserControllerTest(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val baseUrl = "/user"

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 유저 조회 시_유저가 조회된다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().filter { null != it.nickname }.forEach { user ->
            val accessToken = tokenProvider.createAccessToken(id = user.id).token

            mockMvc.perform(get("$baseUrl")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            ).andExpect(status().isOk)
        }
    }

    @Test
    @DisplayName("실패_잘못된 userId를 이용하여 유저 조회 시_404를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val accessToken = tokenProvider.createAccessToken(id = 0).token

        mockMvc.perform(get("$baseUrl")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @DisplayName("userId를 이용하여 닉네임 변경")
    fun update_user_nickname_with_userId() {
        val user = userRepository.findAll()[0]
        val nickname = "changed!"
        val accessToken = tokenProvider.createAccessToken(id = user.id).token

        mockMvc.perform(patch("$baseUrl/nickname")
            .param("nickname", nickname)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(0))
    }
    
    @Test
    @DisplayName("userId를 이용하여 유저를 삭제한다")
    fun delete_user_with_user_id() {
        val user = userRepository.findAll()[0]
        val accessToken = tokenProvider.createAccessToken(id = user.id).token
        
        mockMvc.perform(patch("$baseUrl")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
        
        Assertions.assertThat(user.isDeleted).isTrue()
    }
}

