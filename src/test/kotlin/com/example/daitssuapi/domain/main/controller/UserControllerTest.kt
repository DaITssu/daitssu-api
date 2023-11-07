package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.ControllerTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class UserControllerTest(
    private val userRepository: UserRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val url = "/user"

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 유저 조회 시_유저가 조회된다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().filter { null != it.nickname }.forEach { user ->
            mockMvc.perform(get("$url/${user.id}"))
                .andExpect(status().isOk)
        }
    }

    @Test
    @DisplayName("실패_잘못된 userId를 이용하여 유저 조회 시_404를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0

        mockMvc.perform(get("$url/$wrongUserId"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.data").isEmpty)
    }
    @Test
    @DisplayName("userId를 이용하여 닉네임 변경")
    fun update_user_nickname_with_userId(){
        val user = userRepository.findAll()[0]
        val nickname = "changed!"
        mockMvc.perform(put("$url/nickname")
            .param("userId",user.id.toString())
            .param("nickname", nickname)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data.nickname").value(userRepository.findAll()[0].nickname))

    }
}

