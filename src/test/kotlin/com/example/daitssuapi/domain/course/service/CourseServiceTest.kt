package com.example.daitssuapi.domain.course.service

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.course.dto.request.*
import com.example.daitssuapi.domain.course.model.repository.AssignmentRepository
import com.example.daitssuapi.domain.course.model.repository.CourseNoticeRepository
import com.example.daitssuapi.domain.course.model.repository.CourseRepository
import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@IntegrationTest
class CourseServiceTest(
    private val courseService: CourseService,
    private val courseRepository: CourseRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository,
    private val assignmentRepository: AssignmentRepository,
    private val courseNoticeRepository: CourseNoticeRepository,
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
    fun get_course_list() {
        val courses = courseRepository.findAll()
        val findCourses = courseService.getCourseList()

        assertAll(
            { assertThat(findCourses).isNotEmpty },
            { assertThat(courses.size).isEqualTo(findCourses.size) }
        )
    }

    @Test
    @DisplayName("올바른 courseId를 이용하여 조회시_과목이 조회된다")
    fun get_course_with_course_id() {
        val courseId = 1L
        val course = courseRepository.findByIdOrNull(courseId)
        val findCourse = courseService.getCourse(courseId)

        assertAll(
            { assertThat(findCourse.name).isEqualTo(course?.name) },
            { assertThat(findCourse.term).isEqualTo(course?.term) },
            { assertThat(findCourse.videos.size).isEqualTo(course?.videos?.size) },
        )
    }

    @Test
    @DisplayName("잘못된 courseId를 이용하여 조회시_에러가 출력된다")
    fun get_course_with_wrong_course_id() {
        val wrongCourseId = 0L

        assertThrows<DefaultException> { courseService.getCourse(wrongCourseId) }
    }

    @Test
    @DisplayName("올바른 date 형식으로 캘린더 조회시_일정이 조회된다")
    fun get_calendar_with_date() {
        // case 1. 조회가 잘되는지 확인
        val date = "2023-07"
        val userId = 1L
        val findCalendar = courseService.getCalendar(date, userId)
        
        assertAll(
            { assertThat(findCalendar.size).isEqualTo(1) }
        )

    }

    @Test
    @DisplayName("잘못된 date 형식으로 캘린더 조회시_에러가 발생한다")
    fun get_calendar_with_wrong_date() {
        val date = "2023-07-27"
        val userId = 1L
        
        assertThrows<DefaultException> {
            courseService.getCalendar(dateRequest = date, userId = userId)
        }
    }

    @Test
    @DisplayName("올바른 calendarRequest 요청시_캘린더가 생성된다")
    fun post_create_calendar_with_calendar_request() {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            courseId = 2,
            dueAt = "2023-07-27 23:59:59",
            name = "과제 꼭 하기",
            isCompleted = false
        )
        val userId = 1L
        val findCalendar = courseService.postCalendar(calendarRequest = calendarRequest, userId = userId)
     
        assertAll(
            { assertThat(findCalendar.type).isEqualTo(calendarRequest.type) },
            { assertThat(findCalendar.name).isEqualTo(calendarRequest.name) },
            { assertThat(findCalendar.dueAt).isEqualTo("2023-07-27T23:59:59") },
            { assertThat(findCalendar.isCompleted).isFalse() }
        )
    }

    @Test
    @DisplayName("잘못된 calendarRequest 요청시_에러가 발생한다")
    fun post_create_calendar_with_wrong_calendar_request() {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            courseId = 2,
            dueAt = "2023-07-27",
            name = "과제 꼭 하기",
            isCompleted = false
        )
        val userId = 1L
        
        assertThrows<DefaultException> {
            courseService.postCalendar(calendarRequest = calendarRequest, userId = userId)
        }
    }

    @Test
    @DisplayName("올바른 calendarRequest로 수정 요청시_캘린더가 출력된다")
    fun put_update_calendar_with_calendar_request() {
        val calendarRequestUpdate = CalendarRequest(
            type = CalendarType.ASSIGNMENT,
            courseId = 1,
            dueAt = "2023-08-27 23:59:59",
            name = "과제",
            isCompleted = true
        )

        val updateCalendar = courseService.updateCalendar(calendarRequestUpdate, 13L)

        assertAll(
            { assertThat(updateCalendar.name).isEqualTo(calendarRequestUpdate.name) },
            { assertThat(updateCalendar.type).isEqualTo(calendarRequestUpdate.type) },
            { assertThat(updateCalendar.dueAt).isEqualTo("2023-08-27T23:59:59") },
            { assertThat(updateCalendar.id).isEqualTo(13L) },
            { assertThat(updateCalendar.isCompleted).isTrue() }
        )
    }

    @Test
    @DisplayName("잘못된 calendarRequest, courseId 요청시_에러가 발생한다")
    fun update_create_calendar_with_wrong_calendar_request() {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            courseId = 2,
            dueAt = "2023-07-27",
            name = "과제 꼭 하기",
            isCompleted = true
        )

        assertAll(
            { assertThrows<DefaultException> { courseService.updateCalendar(calendarRequest, 13L) } },
            { assertThrows<DefaultException> { courseService.updateCalendar(calendarRequest, 1L) } }
        )
    }

    @Test
    @DisplayName("오늘 마감인 캘린더 요청시, 과제와 강의가 출력된다.")
    fun get_calendar_with_today_date() {
        val userId = 1L
        
        val calendars = courseService.getTodayDueAtCalendars(userId = userId)

        assertAll(
            { assertThat(calendars.videos.size).isLessThanOrEqualTo(2) },
            { assertThat(calendars.assignments.size).isLessThanOrEqualTo(2) },
        )
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

        val assignmentResponse = courseService.postAssignment(request = request)

        assertAll(
            { assertThat(assignmentResponse.id).isNotZero() },
            { assertThat(assignmentResponse.courseId).isEqualTo(request.courseId) },
            { assertThat(assignmentResponse.name).isEqualTo(request.name) },
            { assertThat(assignmentResponse.dueAt).isEqualTo(request.dueAt) },
            { assertThat(assignmentResponse.startAt).isEqualTo(request.startAt) },
            { assertThat(assignmentResponse.submitAt).isEqualTo(request.submitAt) },
            { assertThat(assignmentResponse.detail).isEqualTo(request.detail) },
            { assertThat(assignmentResponse.comments).isEqualTo(request.comments) }
        )
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

        assertThrows<DefaultException> { courseService.postAssignment(request = request) }
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

        val assignmentResponse = courseService.updateAssignment(request = request)

        assertAll(
            { assertThat(assignmentResponse.id).isEqualTo(request.id) },
            { assertThat(assignmentResponse.dueAt).isEqualTo(assignmentResponse.dueAt) },
            { assertThat(assignmentResponse.comments).isEqualTo(assignmentResponse.comments) }
        )
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

        val courseNoticeResponse = courseService.createNotice(request = request)

        assertAll(
            { assertThat(courseNoticeResponse.id).isNotZero() },
            { assertThat(courseNoticeResponse.courseId).isEqualTo(request.courseId) },
            { assertThat(courseNoticeResponse.isActive).isTrue() },
            { assertThat(courseNoticeResponse.views).isZero() },
            { assertThat(courseNoticeResponse.content).isEqualTo(request.content) },
            { assertThat(courseNoticeResponse.fileUrl).isEqualTo(request.fileUrl ?: emptyList<String>()) },
            { assertThat(courseNoticeResponse.registeredAt).isEqualTo(request.registeredAt) }
        )
    }

    @Test
    @DisplayName("실패_강의가 존재하지 않으면_공지 생성에 실패한다")
    fun failCreateCourseNotice() {
        val request = CourseNoticeCreateRequest(
            courseId = 0L,
            name = "공지이름",
            registeredAt = LocalDateTime.now().minusHours(5),
            content = "공지 내용"
        )

        assertThrows<DefaultException> { courseService.createNotice(request = request) }
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지가 수정된다")
    fun successUpdateCourseNotice() {
        val courseNotice = courseNoticeRepository.findAll()[0]
        val request = CourseNoticeUpdateRequest(
            content = "공지 내용",
            fileUrl = listOf("asdf.png")
        )

        val courseNoticeResponse = courseService.updateNotice(noticeId = courseNotice.id, request = request)

        assertAll(
            { assertThat(courseNoticeResponse.id).isEqualTo(courseNotice.id) },
            { assertThat(courseNoticeResponse.courseId).isEqualTo(courseNotice.course.id) },
            { assertThat(courseNoticeResponse.isActive).isEqualTo(courseNotice.isActive) },
            { assertThat(courseNoticeResponse.views).isEqualTo(courseNotice.views) },
            { assertThat(courseNoticeResponse.content).isEqualTo(request.content) },
            { assertThat(courseNoticeResponse.fileUrl).isEqualTo(request.fileUrl) },
            { assertThat(courseNoticeResponse.registeredAt).isEqualTo(courseNotice.registeredAt) }
        )
    }

    @Test
    @DisplayName("실패_강의의 공지가 없다면_공지 수정에 실패한다")
    fun failUpdateCourseNotice() {
        val request = CourseNoticeUpdateRequest(
            content = "공지 내용",
            fileUrl = listOf("asdf.png")
        )

        assertThrows<DefaultException> { courseService.updateNotice(noticeId = 0L, request = request) }
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지들이 조회된다")
    fun successGetNotices() {
        val course = courseRepository.findAll()[0]

        val courseNoticeResponse = courseService.getNotices(courseId = course.id)

        assertThat(courseNoticeResponse).isNotEmpty
    }

    @Test
    @DisplayName("성공_강의 혹은 공지가 없다면_공지가 조회되지 않는다")
    fun successGetNoticesEmpty() {
        val course = courseRepository.findAll().filter { it.courseNotices.isEmpty() }[0]

        val courseNoticeResponse = courseService.getNotices(courseId = course.id)

        assertThat(courseNoticeResponse).isEmpty()
    }

    @Test
    @DisplayName("성공_올바른 정보를 넘겨줄 시_강의의 공지를 조회하고 조회수가 올라간다")
    fun successGetNotice() {
        val courseNotice = courseNoticeRepository.findAll()[0]
        val originViews = courseNotice.views

        val courseNoticeResponse = courseService.getNotice(courseId = courseNotice.course.id, noticeId = courseNotice.id)

        assertAll(
            { assertThat(courseNoticeResponse.id).isEqualTo(courseNotice.id) },
            { assertThat(courseNoticeResponse.views).isEqualTo(originViews + 1) }
        )
    }

    @Test
    @DisplayName("실패_공지가 없다면_공지 조회에 실패한다")
    fun failGetNotice() {
        val course = courseRepository.findAll()[0]

        assertThrows<DefaultException> { courseService.getNotice(courseId = course.id, noticeId = 0L) }
    }
}
