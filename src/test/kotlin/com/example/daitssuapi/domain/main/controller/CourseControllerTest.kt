package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.utils.ControllerTest
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
    private val userRepository: UserCourseRelationRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val url = "/daitssu/course/user"

//    @Test
//    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
//    fun success_get_course_with_user_id() {
//        userRepository.findAll().forEach { user ->
//            val response = mockMvc.perform(get("$url/${user.id}"))
//                .andExpect(status().isOk)
//                .andReturn().response
//            assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isFalse()
//        }
//    }
//
//    @Test
//    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
//    fun success_get_empty_course_with_wrong_user_id() {
//        val wrongUserId = 0
//
//        val response = mockMvc.perform(get("$url/$wrongUserId"))
//            .andExpect(status().isOk)
//            .andReturn().response
//        assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isTrue
//    }
    // TODO : 현재 Bearer Token을 이용하여 유저를 검증하는데, 소스에 그대로 때려박혀 있기에 실제 유저가 Token을 임의로 조작하여 실 서비스에 영향을 줄 수 있기에 테스트조차 작성 X
}
