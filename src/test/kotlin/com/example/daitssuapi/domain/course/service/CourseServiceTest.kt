package com.example.daitssuapi.domain.course.service

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import com.example.daitssuapi.domain.course.model.repository.CourseRepository
import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

@IntegrationTest
class CourseServiceTest(
    private val courseService: CourseService,
    private val courseRepository: CourseRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        userCourseRelationRepository.findAll().map { it.user.id }.forEach { userId ->
            val courses = userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(userId = userId).filter {
                RegisterStatus.ACTIVE == it.registerStatus
            }

            val findCourses = courseService.getUserCourses(userId = userId)

            assertAll(
                { assertThat(findCourses).isNotEmpty },
                { assertThat(findCourses.size).isEqualTo(courses.size) }
            )
        }
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0L

        val findCourses = courseService.getUserCourses(userId = wrongUserId)

        assertAll(
            { assertThat(findCourses).isEmpty() }
        )
    }
    
    @Test
    @DisplayName("과목 리스트 조회 시_과목 리스트가 출력된다")
    fun get_course_list () {
        val courses = courseRepository.findAll()
        val findCourses = courseService.getCourseList()
        
        assertAll(
            { assertThat(findCourses).isNotEmpty },
            { assertThat(courses.size).isEqualTo(findCourses.size) }
        )
    }
    
    @Test
    @DisplayName("올바른 courseId를 이용하여 조회시_과목이 조회된다")
    fun get_course_with_course_id () {
        val courseId = 1L
        val course = courseRepository.findByIdOrNull(courseId)
        val findCourse = courseService.getCourse(courseId)
        
        assertAll(
            { assertThat(findCourse.name).isEqualTo(course?.name) },
            { assertThat(findCourse.term).isEqualTo(course?.term) },
            { assertThat(findCourse.videos.size).isEqualTo(course?.videos?.size) },
            { assertThat(findCourse.assignments.size).isEqualTo(course?.videos?.size) }
        )
    }
    
    @Test
    @DisplayName("잘못된 courseId를 이용하여 조회시_에러가 출력된다")
    fun get_course_with_wrong_course_id() {
        val wrongCourseId = 0L
        
        org.junit.jupiter.api.assertThrows<DefaultException> {
            courseService.getCourse(wrongCourseId)
        }
        
    }
    
    @Test
    @DisplayName("올바른 date 형식으로 캘린더 조회시_일정이 조회된다")
    fun get_calendar_with_date () {
        // case 1. 조회가 잘되는지 확인
        val date = "2023-07-01 11:00:00"
        val name = "eat paper"
        val findCalendar = courseService.getCalendar(date)
        
        assertAll(
            { assertThat(findCalendar.keys).contains(name) },
            { assertThat(findCalendar[name]?.size).isEqualTo(2) }
        )
        
        // case2 경계값
        val date2 = "2023-05-01 00:00:00"
        val name2 = "choco"
        val findCalendar2 = courseService.getCalendar(date2)

        assertAll(
            { assertThat(findCalendar2.keys).contains(name2) },
            { assertThat(findCalendar2[name2]?.size).isEqualTo(1) }
        )
    }
    
    @Test
    @DisplayName("잘못된 date 형식으로 캘린더 조회시_에러가 발생한다")
    fun get_calendar_with_wrong_date() {
        val date = "2023-07-27"
        
        org.junit.jupiter.api.assertThrows<DefaultException> {
            courseService.getCalendar(date)
        }
    }
    
    @Test
    @DisplayName("올바른 calendarRequest 요청시_캘린더가 생성된다")
    fun post_create_calendar_with_calendar_request() {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            course = "do it",
            dueAt = "2023-07-27 23:59:59",
            name = "과제 꼭 하기"
        )
        val findCalendar = courseService.postCalendar(calendarRequest)
     
        assertAll(
            { assertThat(findCalendar.type).isEqualTo(calendarRequest.type) },
            { assertThat(findCalendar.name).isEqualTo(calendarRequest.name) },
            { assertThat(findCalendar.dueAt).isEqualTo("2023-07-27T23:59:59") }
        )
    }
    
    @Test
    @DisplayName("잘못된 calendarRequest 요청시_에러가 발생한다")
    fun post_create_calendar_with_wrong_calendar_request() {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            course = "do it",
            dueAt = "2023-07-27",
            name = "과제 꼭 하기"
        )
        
        org.junit.jupiter.api.assertThrows<DefaultException> {
            courseService.postCalendar(calendarRequest)
        }
    }
    
    @Test
    @DisplayName("올바른 calendarRequest로 수정 요청시_캘린더가 출력된다")
    fun put_update_calendar_with_calendar_request() {
        val calendarRequestUpdate = CalendarRequest(
            type = CalendarType.ASSIGNMENT,
            course = "JAVA",
            dueAt = "2023-08-27 23:59:59",
            name = "과제"
        )
        
        val updateCalendar = courseService.updateCalendar(calendarRequestUpdate, 13L)
        
        assertAll(
            { assertThat(updateCalendar.name).isEqualTo(calendarRequestUpdate.name) },
            { assertThat(updateCalendar.type).isEqualTo(calendarRequestUpdate.type) },
            { assertThat(updateCalendar.dueAt).isEqualTo("2023-08-27T23:59:59") },
            { assertThat(updateCalendar.id).isEqualTo(13L) }
        )
    }
    
    @Test
    @DisplayName("잘못된 calendarRequest, courseId 요청시_에러가 발생한다")
    fun update_create_calendar_with_wrong_calendar_request() {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            course = "do it",
            dueAt = "2023-07-27",
            name = "과제 꼭 하기"
        )
        
        assertAll(
            { org.junit.jupiter.api.assertThrows<DefaultException> {
                    courseService.updateCalendar(calendarRequest, 13L)
                } },
            { org.junit.jupiter.api.assertThrows<DefaultException> {
                courseService.updateCalendar(calendarRequest, 1L)
            } }
        )
        
    }
    
}
