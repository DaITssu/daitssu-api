package com.example.daitssuapi.domain.course.controller

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.utils.ControllerTest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class CourseControllerTest(
    private val userRepository: UserCourseRelationRepository
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val url = "/course/user"
    private val courseUrl = "/course"

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().forEach { user ->
            mockMvc.perform(get("$url/${user.id}"))
                .andExpect(status().isOk)
        }
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0

        mockMvc.perform(get("$url/$wrongUserId"))
        mockMvc.perform(get("$url/$wrongUserId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }
    
    @Test
    @DisplayName("과목 리스트 조회시_모든 과목이 조회된다")
    fun get_course_list() {
        val response = mockMvc.perform(get("$courseUrl"))
            .andExpect(status().isOk)
            .andReturn().response
        
        assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isFalse()
    }
    
    @Test
    @DisplayName("올바른 courseId로 과목 조회시_과목에 대한 정보가 조회된다")
    fun get_course_with_course_id () {
        val courseId = 1L
        val response = mockMvc.perform(get("$courseUrl/$courseId"))
            .andExpect(status().isOk)
            .andReturn().response
        
        assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isFalse()
    }
    
    @Test
    @DisplayName("올바르지 않은 courseId로 과목 조회시_에러가 발생한다")
    fun get_course_with_wrong_course_id () {
        val wrongCourseId = 0L
        val response = mockMvc.perform(get("$courseUrl/$wrongCourseId"))
            .andExpect(status().isBadRequest)
            .andReturn().response
        
        val responseBody = jacksonObjectMapper().readTree(response.contentAsByteArray)
        
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.COURSE_NOT_FOUND.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.COURSE_NOT_FOUND.message) },
            { assertThat(responseBody["data"].isNull).isTrue() }
        )
    }
    
    @Test
    @DisplayName("올바른 date로 캘린더 조회시_캘린더에 대한 정보가 조회된다")
    fun get_calendar_with_date() {
        val date = "2023-07-31 23:59:59"
        val response = mockMvc.perform(get("$courseUrl/calendar/$date"))
            .andExpect(status().isOk)
            .andReturn().response
        
        assertThat(jacksonObjectMapper().readTree(response.contentAsString).isEmpty).isFalse()
    }
    
    @Test
    @DisplayName("잘못된 date로 캘린더 조회시_에러가 출력된다")
    fun get_calendar_with_wrong_date () {
        val wrongDate = "2023-07-31"
        val response = mockMvc.perform(get("$courseUrl/calendar/$wrongDate"))
            .andExpect(status().isBadRequest)
            .andReturn().response
        
        val responseBody = jacksonObjectMapper().readTree(response.contentAsByteArray)
        
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.message) },
            { assertThat(responseBody["data"].isNull).isTrue() }
        )
    }
    
    @Test
    @DisplayName("올바른 date가 포함된 calendarRequest로 post시_캘린더가 생성된다")
    fun post_create_calendar_with_calendar_request() {
        val calendarRequest = CalendarRequest(
            name = "숙제 마감일",
            type = CalendarType.ASSIGNMENT,
            course = "do it",
            dueAt = "2023-07-27 23:59:59"
        )
        val response = mockMvc.perform(
            post("$courseUrl/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(calendarRequest))
        )
            .andExpect(status().isOk)
            .andReturn().response
        
        assertThat(jacksonObjectMapper().readTree(response.contentAsString)["data"].isEmpty).isFalse()
    }
    
    @Test
    @DisplayName("잘못된 date가 포함된 calendarRequest로 post시_에러가 발생한다")
    fun post_create_calendar_with_wrong_calendar_request() {
        val calendarRequest = CalendarRequest(
            name = "강의 출석 마감일",
            type = CalendarType.VIDEO,
            course = "choco",
            dueAt = "2023-07-27"
        )
        val response = mockMvc.perform(
            post("$courseUrl/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(calendarRequest))
        )
            .andExpect(status().isBadRequest)
            .andReturn().response
        
        val responseBody = jacksonObjectMapper().readTree(response.contentAsByteArray)
        
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.message) },
            { assertThat(responseBody["data"].isNull).isTrue() }
        )
        
    }
}
