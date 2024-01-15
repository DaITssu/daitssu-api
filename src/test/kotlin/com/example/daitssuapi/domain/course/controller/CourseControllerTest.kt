package com.example.daitssuapi.domain.course.controller

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.objectMapper
import com.example.daitssuapi.common.security.component.TokenProvider
import com.example.daitssuapi.domain.course.dto.request.AssignmentCreateRequest
import com.example.daitssuapi.domain.course.dto.request.AssignmentUpdateRequest
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import com.example.daitssuapi.domain.course.dto.request.CourseNoticeCreateRequest
import com.example.daitssuapi.domain.course.dto.request.CourseNoticeUpdateRequest
import com.example.daitssuapi.domain.course.model.repository.AssignmentRepository
import com.example.daitssuapi.domain.course.model.repository.CourseNoticeRepository
import com.example.daitssuapi.domain.course.model.repository.CourseRepository
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import com.example.daitssuapi.utils.ControllerTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@ControllerTest
class CourseControllerTest(
    private val courseNoticeRepository: CourseNoticeRepository,
    private val assignmentRepository: AssignmentRepository,
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider
) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val baseUrl = "/course"

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().forEach { user ->
            val accessToken = tokenProvider.createAccessToken(id = user.id).token

            mockMvc.perform(get("$baseUrl/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            ).andExpect(status().isOk)
        }
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val accessToken = tokenProvider.createAccessToken(id = 0).token

        mockMvc.perform(get("$baseUrl/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @DisplayName("과목 리스트 조회시_모든 과목이 조회된다")
    fun get_course_list() {
        val response = mockMvc.perform(get(baseUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andReturn().response

        assertThat(objectMapper.readTree(response.contentAsString)["data"].isEmpty).isFalse
    }

    @Test
    @DisplayName("올바른 courseId로 과목 조회시_과목에 대한 정보가 조회된다")
    fun get_course_with_course_id() {
        val courseId = 1L

        val response = mockMvc.perform(get("$baseUrl/$courseId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andReturn().response

        assertThat(objectMapper.readTree(response.contentAsString)["data"].isEmpty).isFalse
    }

    @Test
    @DisplayName("올바르지 않은 courseId로 과목 조회시_에러가 발생한다")
    fun get_course_with_wrong_course_id() {
        val wrongCourseId = 0L

        val response = mockMvc.perform(get("$baseUrl/$wrongCourseId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andReturn().response

        val responseBody = objectMapper.readTree(response.contentAsByteArray)
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.COURSE_NOT_FOUND.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.COURSE_NOT_FOUND.message) },
            { assertThat(responseBody["data"].isNull).isTrue() }
        )
    }

    @Test
    @DisplayName("올바른 date와 userId로 캘린더 조회시_캘린더에 대한 정보가 조회된다")
    fun get_calendar_with_date() {
        val date = "2023-07"
        val accessToken = tokenProvider.createAccessToken(id = 1L).token

        val response = mockMvc.perform(get("$baseUrl/calendar?date=$date")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andReturn().response

        assertThat(objectMapper.readTree(response.contentAsString).isEmpty).isFalse
    }

    @Test
    @DisplayName("잘못된 date로 캘린더 조회시_에러가 출력된다")
    fun get_calendar_with_wrong_date() {
        val wrongDate = "2023-07-31"

        val response = mockMvc.perform(get("$baseUrl/calendar?date=$wrongDate")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andReturn().response

        val responseBody = objectMapper.readTree(response.contentAsByteArray)
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.INVALID_GET_DATE_FORMAT.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.INVALID_GET_DATE_FORMAT.message) },
            { assertThat(responseBody["data"].isNull).isTrue() }
        )
    }

    @Test
    @DisplayName("올바른 date가 포함된 calendarRequest로 post시_캘린더가 생성된다")
    fun post_create_calendar_with_calendar_request() {
        val calendarRequest = CalendarRequest(
            name = "숙제 마감일",
            type = CalendarType.ASSIGNMENT,
            courseId = 2,
            dueAt = "2023-07-27 23:59:59",
            isCompleted = false
        )

        val response = mockMvc.perform(
            post("$baseUrl/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarRequest))
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andReturn().response

        assertThat(objectMapper.readTree(response.contentAsString)["data"].isEmpty).isFalse
    }

    @Test
    @DisplayName("잘못된 date가 포함된 calendarRequest로 post시_에러가 발생한다")
    fun post_create_calendar_with_wrong_calendar_request() {
        val calendarRequest = CalendarRequest(
            name = "강의 출석 마감일",
            type = CalendarType.VIDEO,
            courseId = 4,
            dueAt = "2023-07-27",
            isCompleted = false
        )

        val response = mockMvc.perform(
            post("$baseUrl/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarRequest))
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andReturn().response

        val responseBody = objectMapper.readTree(response.contentAsByteArray)
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.message) },
            { assertThat(responseBody["data"].isNull).isTrue() }
        )

    }

    @Test
    @DisplayName("올바른 date가 포함된 calendarRequest로 put 요청시_캘린더가 수정된다")
    fun put_update_calendar_with_calendar_request() {
        val calendarId = 13L
        val calendarRequest = CalendarRequest(
            name = "숙제 마감일",
            type = CalendarType.ASSIGNMENT,
            courseId = 2,
            dueAt = "2023-07-27 23:59:59",
            isCompleted = true
        )

        val response = mockMvc.perform(
            put("$baseUrl/calendar/$calendarId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarRequest))
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andReturn().response

        assertThat(objectMapper.readTree(response.contentAsString)["data"].isEmpty).isFalse
    }

    @Test
    @DisplayName("잘못된 date가 포함된 calendarRequest, 잘못된 calendarId로 put 요청시_에러가 발생한다")
    fun put_update_calendar_with_wrong_calendar_request_or() {
        val courseId = 13L
        val wrongCourseId = 999L
        val calendarRequest = CalendarRequest(
            name = "강의 출석 마감일",
            type = CalendarType.VIDEO,
            courseId = 4,
            dueAt = "2023-07-27",
            isCompleted = true
        )

        val response = mockMvc.perform(
            put("$baseUrl/calendar/$courseId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarRequest))
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andReturn().response
        val responseWrongId = mockMvc.perform(
            put("$baseUrl/calendar/$wrongCourseId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarRequest))
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andReturn().response

        val responseBody = objectMapper.readTree(response.contentAsByteArray)
        val responseBodyWrongId = objectMapper.readTree(responseWrongId.contentAsByteArray)
        assertAll(
            { assertThat(responseBody["code"].intValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.code) },
            { assertThat(responseBody["message"].textValue()).isEqualTo(ErrorCode.INVALID_DATE_FORMAT.message) },
            { assertThat(responseBody["data"].isNull).isTrue() },
            { assertThat(responseBodyWrongId["code"].intValue()).isEqualTo(ErrorCode.CALENDAR_NOT_FOUND.code) },
            { assertThat(responseBodyWrongId["message"].textValue()).isEqualTo(ErrorCode.CALENDAR_NOT_FOUND.message) },
            { assertThat(responseBodyWrongId["data"].isNull).isTrue() }
        )
    }

    @Test
    @DisplayName("오늘 마감하는 과제, 강의 요청시_캘린더가 출력된다")
    fun get_today_calendar() {
        val accessToken = tokenProvider.createAccessToken(1L).token
        
        val response = mockMvc.perform(get("$baseUrl/calendar:today")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        ).andExpect(status().isOk)
            .andReturn().response

        assertThat(objectMapper.readTree(response.contentAsString)["data"].isEmpty).isFalse
    }

    @Test
    @DisplayName("성공_올바른 강의를 이용하여 과제 생성 시_과제가 생성된다")
    fun success_create_assignment() {
        val course = courseRepository.findAll()[0]
        val request = AssignmentCreateRequest(
            courseId = course.id,
            name = "과제이름",
            dueAt = LocalDateTime.now().plusDays(6),
            startAt = LocalDateTime.now().minusHours(3),
            detail = "과제 상세 내용"
        )

        mockMvc.perform(post("$baseUrl/assignment")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("실패_올바른 강의가 넘어오지 않으면_과제가 생성되지 않는다")
    fun fail_create_assignment() {
        val request = AssignmentCreateRequest(
            courseId = 0L,
            name = "과제이름",
            dueAt = LocalDateTime.now().minusDays(6),
            startAt = LocalDateTime.now().minusHours(3),
            detail = "과제 상세 내용"
        )

        mockMvc.perform(post("$baseUrl/assignment")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.COURSE_NOT_FOUND.code))
    }

    @Test
    @DisplayName("성공_올바른 과제를 이용하여 과제 수정 시_과제가 수정된다")
    fun success_update_assignment() {
        val assignment = assignmentRepository.findAll()[0]
        val request = AssignmentUpdateRequest(
            id = assignment.id,
            dueAt = assignment.dueAt?.plusDays(2) ?: LocalDateTime.now(),
            comments = "대충 추가 내용이라는 뜻"
        )

        mockMvc.perform(put("$baseUrl/assignment")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지가 생성된다")
    fun successCreateCourseNotice() {
        val course = courseRepository.findAll()[0]
        val request = CourseNoticeCreateRequest(
            courseId = course.id,
            name = "공지이름",
            registeredAt = LocalDateTime.now().minusHours(5),
            content = "공지 내용"
        )

        mockMvc.perform(post("$baseUrl/${course.id}/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("실패_강의가 존재하지 않으면_공지 생성에 실패한다")
    fun failCreateCourseNotice() {
        val wrongCourseId = 0L
        val request = CourseNoticeCreateRequest(
            courseId = 0L,
            name = "공지이름",
            registeredAt = LocalDateTime.now().minusHours(5),
            content = "공지 내용"
        )

        mockMvc.perform(post("$baseUrl/$wrongCourseId/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.COURSE_NOT_FOUND.code))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지가 수정된다")
    fun successUpdateCourseNotice() {
        val courseNotice = courseNoticeRepository.findAll()[0]
        val request = CourseNoticeUpdateRequest(
            content = "공지 내용",
            fileUrl = listOf("asdf.png")
        )

        mockMvc.perform(put("$baseUrl/${courseNotice.course.id}/notices/${courseNotice.id}")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("실패_강의의 공지가 없다면_공지 수정에 실패한다")
    fun failUpdateCourseNotice() {
        val course = courseRepository.findAll()[0]
        val wrongCourseNoticeId = 0L
        val request = CourseNoticeUpdateRequest(
            content = "공지 내용",
            fileUrl = listOf("asdf.png")
        )

        mockMvc.perform(put("$baseUrl/${course.id}/notices/$wrongCourseNoticeId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.NOTICE_NOT_FOUND.code))
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지들이 조회된다")
    fun successGetNotices() {
        val course = courseRepository.findAll()[0]

        mockMvc.perform(get("$baseUrl/${course.id}/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @DisplayName("성공_강의 혹은 공지가 없다면_공지가 조회되지 않는다")
    fun successGetNoticesEmpty() {
        val course = courseRepository.findAll().filter { it.courseNotices.isEmpty() }[0]

        mockMvc.perform(get("$baseUrl/${course.id}/notices")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지를 조회하고 조회수가 올라간다")
    fun successGetNotice() {
        val courseNotice = courseNoticeRepository.findAll()[0]
        val originViews = courseNotice.views

        mockMvc.perform(get("$baseUrl/${courseNotice.course.id}/notices/${courseNotice.id}")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data.views").value(originViews + 1))
    }

    @Test
    @DisplayName("실패_공지가 없다면_공지 조회에 실패한다")
    fun failGetNotice() {
        val course = courseRepository.findAll()[0]
        val wrongCourseNoticeId = 0L

        mockMvc.perform(get("$baseUrl/${course.id}/notices/$wrongCourseNoticeId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value(ErrorCode.NOTICE_NOT_FOUND.code))
    }
}
