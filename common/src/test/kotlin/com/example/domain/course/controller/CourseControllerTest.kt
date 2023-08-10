package com.example.domain.course.controller

import com.example.domain.main.model.repository.UserRepository
import com.example.utils.ControllerTest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class CourseControllerTest(
    private val userRepository: UserRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val url = "/daitssu/course"

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().forEach { user ->
            val response = mockMvc.perform(get("$url/${user.id}"))
                .andExpect(status().isOk)
                .andReturn().response
            assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isFalse()
        }
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0

        val response = mockMvc.perform(get("$url/$wrongUserId"))
            .andExpect(status().isOk)
            .andReturn().response
        assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isTrue
    }
}
