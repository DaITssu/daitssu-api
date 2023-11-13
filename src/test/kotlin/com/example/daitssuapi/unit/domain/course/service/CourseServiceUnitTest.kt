package com.example.daitssuapi.unit.domain.course.service

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.course.dto.request.AssignmentRequest
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import com.example.daitssuapi.domain.course.dto.request.CourseRequest
import com.example.daitssuapi.domain.course.dto.request.VideoRequest
import com.example.daitssuapi.domain.course.dto.response.AssignmentResponse
import com.example.daitssuapi.domain.course.dto.response.CalendarResponse
import com.example.daitssuapi.domain.course.dto.response.CourseResponse
import com.example.daitssuapi.domain.course.dto.response.VideoResponse
import com.example.daitssuapi.domain.course.model.entity.*
import com.example.daitssuapi.domain.course.model.repository.*
import com.example.daitssuapi.domain.course.service.CourseService
import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.utils.UnitTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@UnitTest
class CourseServiceUnitTest {
    private val assignmentRepository: AssignmentRepository = mockk(relaxed = true)
    private val courseRepository: CourseRepository = mockk(relaxed = true)
    private val videoRepository: VideoRepository = mockk(relaxed = true)
    private val calendarRepository: CalendarRepository = mockk(relaxed = true)
    private val userCourseRelationRepository: UserCourseRelationRepository = mockk(relaxed = true)
    private val courseService: CourseService = spyk(
        objToCopy = CourseService(assignmentRepository, courseRepository, videoRepository, calendarRepository, userCourseRelationRepository),
        recordPrivateCalls = true
    )

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        val department = Department("computer")
        val user = User(studentId = 20230803, name = "user", department = department, term = 1)
        val course = Course("eat anything", 16)
        every { userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(any()) }.returns(listOf(UserCourseRelation(user, course, RegisterStatus.ACTIVE)))

        val findCourses = courseService.getUserCourses(userId = user.id)

        assertAll(
            { assertThat(findCourses).isNotEmpty },
            { assertThat(findCourses.size).isEqualTo(1) }
        )
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0L
        every { userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(any()) }.returns(emptyList())

        val findCourses = courseService.getUserCourses(userId = wrongUserId)

        assertAll(
            { assertThat(findCourses).isEmpty() }
        )
    }
    
    @Test
    @DisplayName("과목 조회 시 모든 리스트가 출력 된다")
    fun get_course_list() {
        val courses = listOf(Course(name = "kotlin", term = 8), Course(name = "java", term = 4))
        every { courseRepository.findAll() } returns courses
        
        val getCourses = courseService.getCourseList()
        val expectedCourses = courses.map { CourseResponse(name = it.name, term =  it.term, id = it.id) }
        
        assertThat(expectedCourses).isEqualTo(getCourses)
    }
    
    @Test
    @DisplayName("올바른 courseId로 조회 시 course에 대한 정보가 올바르게 전달된다")
    fun get_course_with_course_id() {
        val courseId = 0L
        val video =  mutableListOf(Video(
            name = "kotlin-1강",
            startAt = LocalDateTime.of(2023, 8, 18, 10, 0),
            dueAt = LocalDateTime.of(2023, 8, 25, 10, 0),
        ))
        val assignment = mutableListOf(Assignment(
            name = "kotlin-첫번째 과제",
            startAt = LocalDateTime.of(2023, 8, 18, 10, 0),
            dueAt = LocalDateTime.of(2023, 8, 25, 10, 0),
        ))
        val course = Course(name = "kotlin", term = 8, videos = video, assignments = assignment)
        
        every { courseRepository.findByIdOrNull(any()) } returns course
        
        val result = courseService.getCourse(courseId = courseId)
        val expectedVideo = listOf(VideoResponse(
            id = 0L,
            name = "kotlin-1강",
            startAt = LocalDateTime.of(2023, 8, 18, 10, 0),
            dueAt = LocalDateTime.of(2023, 8, 25, 10, 0),
        ))
        val expectedAssignment = listOf(AssignmentResponse(
            id = 0L,
            name = "kotlin-첫번째 과제",
            startAt = LocalDateTime.of(2023, 8, 18, 10, 0),
            dueAt = LocalDateTime.of(2023, 8, 25, 10, 0),
        ))
        val expectedCourse = CourseResponse(
            name = "kotlin",
            term = 8,
            videos = expectedVideo,
            assignments = expectedAssignment,
            id = courseId
        )
        
        
        assertThat(expectedCourse).isEqualTo(result)
        
    }
    
    @Test
    @DisplayName("잘못된 courseId로 조회시 에러가 발생한다")
    fun get_course_with_wrong_course_id() {
        val wrongCourseId = 999L
        
        every { courseRepository.findByIdOrNull(any()) } returns null
        
        assertThrows<DefaultException> {
            courseService.getCourse(wrongCourseId)
        }
    }
    
    @Test
    @DisplayName("날짜로 해당 월 캘린더 조회하면 캘린더 리스트가 출력된다")
    fun get_calendar_with_local_date_time () {
        val calendar = listOf(Calendar(
            course = "kotlin",
            type = CalendarType.ASSIGNMENT,
            name = "kotlin-첫번째 과제",
            dueAt = LocalDateTime.of(2023,2,28,23,59,59),
            isComplete = false
        ))
        
        every { calendarRepository.findByDueAtBetween(any(), any()) } returns calendar
        
        val result = courseService.getCalendar("2023-02")
        val expectedCalendar = mapOf("kotlin" to
            calendar.map {
                CalendarResponse(
                    name = it.name,
                    dueAt = it.dueAt,
                    type = it.type,
                    id = it.id,
                    isCompleted = it.isComplete
                )
            }
        )
        
        assertThat(expectedCalendar).isEqualTo(result)
    }
    
    @Test
    @DisplayName("잘못된 날짜 포맷으로 캘린더 조회하면 에러가 발생한다")
    fun get_calendar_with_invalidate_local_date_time() {
        val date = "2023 02"
        val expectedErrorCode = ErrorCode.INVALID_GET_DATE_FORMAT
        
        val result = assertThrows<DefaultException> {
            courseService.getCalendar(date)
        }
        
        assertThat(expectedErrorCode).isEqualTo(result.errorCode)
    }
    
    @Test
    @DisplayName("캘린더 생성하면 생성된 캘린더 정보를 출력한다")
    fun post_calendar () {
        val calendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            course = "kotlin",
            dueAt = "2023-02-28 23:59:59",
            name = "4주차 강의",
            isCompleted = false
        )
        val expectedCalendar = CalendarResponse (
            type = calendarRequest.type,
            dueAt = LocalDateTime.of(2023,2,28,23,59,59),
            name = calendarRequest.name,
            id = 0L,
            isCompleted = false
        )
        
        every{ calendarRepository.save(any()) } answers {
            Calendar(
                type = calendarRequest.type,
                course = calendarRequest.course,
                dueAt = LocalDateTime.of(2023,2,28,23,59,59),
                name = calendarRequest.name,
                isComplete = calendarRequest.isCompleted
            )
        }
        
        val result = courseService.postCalendar(calendarRequest)
        
        assertThat(expectedCalendar).isEqualTo(result)
    }
    
    @Test
    @DisplayName("잘못된 날짜 형식으로 캘린더 생성하면 에러가 발생한다")
    fun post_calendar_with_invalidate_local_date_time() {
        val invalidCalendarRequest = CalendarRequest(
            type = CalendarType.VIDEO,
            course = "kotlin",
            dueAt = "2023-08-15",
            name = "8주차 강의",
            isCompleted = false
        )
        val expectedErrorCode = ErrorCode.INVALID_DATE_FORMAT
        
        val result = assertThrows<DefaultException> { courseService.postCalendar(invalidCalendarRequest) }
        
        assertThat(expectedErrorCode).isEqualTo(result.errorCode)
    }
    
    @Test
    @DisplayName("강의 생성하면 생성된 강의 정보를 출력한다")
    fun post_video() {
        val course = Course(name = "kotlin", term = 8)
        val videoRequest = VideoRequest(courseId = 0L, name = "1강")
        
        every { courseRepository.findByIdOrNull(any()) } returns course
        every { videoRepository.save(any()) } answers {
            Video(
                name = videoRequest.name,
                course = course,
                startAt = LocalDateTime.now(),
                dueAt = LocalDateTime.now().plusDays(7)
            )
        }
        
        val result = courseService.postVideo(videoRequest)
        
        
        val expectedVideo = VideoResponse(
            id = result.id,
            name = videoRequest.name,
            dueAt = result.dueAt,
            startAt = result.startAt
        )
        
        assertThat(expectedVideo).isEqualTo(result)
    }
    
    
    @Test
    @DisplayName("잘못된 과목 id로 강의 생성하면 에러가 발생한다")
    fun post_video_with_wrong_course_id () {
        val wrongCourseId = 999L
        val videoRequest = VideoRequest(courseId = wrongCourseId, name = "1강")
        
        every { courseRepository.findByIdOrNull(any()) } returns null
        
        val result = assertThrows<DefaultException> { courseService.postVideo(videoRequest) }
        
        val expectedErrorCode = ErrorCode.COURSE_NOT_FOUND
        
        assertThat(expectedErrorCode).isEqualTo(result.errorCode)
    }
    
    
    @Test
    @DisplayName("과제 생성하면 생성된 과제 정보가 출력된다")
    fun post_assignment() {
        val course = Course(name = "kotlin", term = 8)
        val assignmentRequest = AssignmentRequest(courseId = 0L, name = "첫번째 과제")
        
        every { courseRepository.findByIdOrNull(any()) } returns course
        every { assignmentRepository.save(any()) } answers {
            Assignment(
                name = assignmentRequest.name,
                course = course,
                startAt = LocalDateTime.now(),
                dueAt = LocalDateTime.now().plusDays(7)
            )
        }
        
        val result = courseService.postAssignment(assignmentRequest)
        val expectedAssignment = AssignmentResponse(
            id = result.id,
            name = assignmentRequest.name,
            startAt = result.startAt,
            dueAt = result.dueAt
        )
        
        assertThat(expectedAssignment).isEqualTo(result)
    }
    
    @Test
    @DisplayName("잘못된 과목 id로 과제를 생성하면 에러가 난다")
    fun post_assignment_with_wrong_course_id () {
        val wrongCourseId = 999L
        val assignmentRequest = AssignmentRequest(courseId = wrongCourseId, name = "첫번째 과제")
        
        every { courseRepository.findByIdOrNull(any()) } returns null
        
        val result = assertThrows<DefaultException> { courseService.postAssignment(assignmentRequest) }
        val expectedErrorCode = ErrorCode.COURSE_NOT_FOUND
        
        assertThat(expectedErrorCode).isEqualTo(result.errorCode)
    }
    
    @Test
    @DisplayName("과목을 생성하면 과목에 대한 정보가 출력된다")
    fun post_course() {
        val courseRequest = CourseRequest(name = "kotlin", term = 8)
        
        every { courseRepository.save(any()) } answers {
            Course(name = courseRequest.name, term = courseRequest.term)
        }
        
        val result = courseService.postCourse(courseRequest)
        val expectedCourse = CourseResponse (name = courseRequest.name, term = courseRequest.term, id =  0)
        
        assertThat(expectedCourse).isEqualTo(result)
    }
    
    @Test
    @DisplayName("캘린더를 수정하면 정보가 바뀐다")
    fun put_update_calendar() {
        val calendar = Calendar(
            type = CalendarType.VIDEO,
            course = "Kotlin",
            dueAt = LocalDateTime.of(2023,7,27,23,59,59),
            name = "4주차 강의",
            isComplete = false
        )
        val calendarRequest = CalendarRequest(
            type = CalendarType.ASSIGNMENT,
            course = "JAVA",
            dueAt = "2023-08-28 23:59:59",
            name = "8주차 강의",
            isCompleted = true
        )
        val updateCalendar = Calendar(
            type = CalendarType.ASSIGNMENT,
            course = "JAVA",
            dueAt = LocalDateTime.of(2023,8,28,23,59,59),
            name = "8주차 강의",
            isComplete = true
        )
        
        every { calendarRepository.findByIdOrNull(any()) } returns calendar
        every { calendarRepository.save(any()) } returns updateCalendar
        
        val findCalendar = courseService.updateCalendar(calendarRequest, 13L)
        
        assertAll(
            { assertThat(findCalendar.name).isEqualTo(calendarRequest.name) },
            { assertThat(findCalendar.dueAt).isEqualTo("2023-08-28T23:59:59") },
            { assertThat(findCalendar.type).isEqualTo(calendarRequest.type) },
            { assertThat(findCalendar.isCompleted).isTrue()}
        )
    }
    
    @Test
    @DisplayName("잘못된 calendarId, date로 요청하면 에러가 난다")
    fun put_update_calendar_with_wrong_calendar_id_or_date() {
        val wrongCalendarId = 999L
        val courseId = 13L
        
        val calendar = Calendar(
            type = CalendarType.VIDEO,
            course = "Kotlin",
            dueAt = LocalDateTime.of(2023,7,27,23,59,59),
            name = "4주차 강의",
            isComplete = false
        )
        
        val wrongCalendarRequest = CalendarRequest(
            type = CalendarType.ASSIGNMENT,
            course = "JAVA",
            dueAt = "2023-08-28",
            name = "8주차 강의",
            isCompleted = true
        )
        
        val calendarRequest = CalendarRequest(
            type = CalendarType.ASSIGNMENT,
            course = "JAVA",
            dueAt = "2023-08-28 23:59:59",
            name = "8주차 강의",
            isCompleted = true
        )
        
        every { calendarRepository.findByIdOrNull(courseId) } returns calendar
        every { calendarRepository.findByIdOrNull(wrongCalendarId) } returns null
        
        val result = assertThrows<DefaultException> {
            courseService.updateCalendar(wrongCalendarRequest, courseId)
        }
        val resultWrongId = assertThrows<DefaultException> {
            courseService.updateCalendar(calendarRequest, wrongCalendarId)
        }
        
        assertAll(
            { assertThat(result.errorCode).isEqualTo(ErrorCode.INVALID_DATE_FORMAT) },
            { assertThat(resultWrongId.errorCode).isEqualTo(ErrorCode.CALENDAR_NOT_FOUND)}
        )
        
    }
    
    @Test
    @DisplayName("오늘 마감인 과제, 강의가 있으면 올바르게 출력된다")
    fun get_today_calendar_not_empty() {
        val videoCourses = listOf(
            Calendar(
                course = "just",
                dueAt = LocalDateTime.of(2023,10,2,9,0,0),
                type = CalendarType.VIDEO,
                name = "강의 1",
                isComplete = false
            ),
            Calendar(
                course = "do it",
                dueAt = LocalDateTime.of(2023,10,2,16,0,0),
                type = CalendarType.VIDEO,
                name = "강의 1",
                isComplete = false
            )
        )
        
        val assignmentCourses = listOf(
            Calendar(
                course = "i study",
                dueAt = LocalDateTime.of(2023,10,2,14,0,0),
                type = CalendarType.ASSIGNMENT,
                name = "강의 1",
                isComplete = false
            )
        )
        
        val justVideos = listOf(
            Calendar(
                course = "just",
                dueAt = LocalDateTime.of(2023,10,2,14,0,0),
                type = CalendarType.VIDEO,
                name = "강의 1",
                isComplete = false
            ),
            Calendar(
                course = "just",
                dueAt = LocalDateTime.of(2023,10,2,23,59,59),
                type = CalendarType.VIDEO,
                name = "강의 2",
                isComplete = true
            )
        )
        
        val doitCalendars = listOf(
            Calendar(
                course = "do it",
                dueAt = LocalDateTime.of(2023,10,2,16,0,0),
                type = CalendarType.VIDEO,
                name = "강의 1",
                isComplete = false
            )
        )
        
        val iStudyVideos = listOf(
            Calendar(
                course = "i study",
                dueAt = LocalDateTime.of(2023,10,2,14,0,0),
                type = CalendarType.ASSIGNMENT,
                name = "강의 1",
                isComplete = false
            ),
            Calendar(
                course = "i study",
                dueAt = LocalDateTime.of(2023,10,2,23,59,59),
                type = CalendarType.ASSIGNMENT,
                name = "강의 2",
                isComplete = true
            )
        )
        
        
        every { calendarRepository.findDistinctTop2ByTypeAndDueAtBetweenOrderByDueAtAsc(
            type = CalendarType.VIDEO, any(), any()) } returns videoCourses
        every { calendarRepository.findDistinctTop2ByTypeAndDueAtBetweenOrderByDueAtAsc(
            type = CalendarType.ASSIGNMENT, any(), any()) } returns assignmentCourses
        every { calendarRepository.findByTypeAndCourseAndDueAtBetween(
            type = CalendarType.VIDEO, course = "just", any(), any()) } returns justVideos
        every { calendarRepository.findByTypeAndCourseAndDueAtBetween(
            type = CalendarType.VIDEO, course = "do it", any(), any()) } returns doitCalendars
        every { calendarRepository.findByTypeAndCourseAndDueAtBetween(
            type = CalendarType.ASSIGNMENT, course = "i study", any(), any()) } returns iStudyVideos
        
        val todayCalendar = courseService.getTodayDueAtCalendars()
        
        assertAll(
            { assertThat(todayCalendar.videos.size).isEqualTo(2) },
            { assertThat(todayCalendar.assignments.size).isEqualTo(1) },
            { assertThat(todayCalendar.videos.get(0).course).isEqualTo("just") },
            { assertThat(todayCalendar.videos.get(0).count).isEqualTo(2) },
            { assertThat(todayCalendar.videos.get(0).dueAt).isEqualTo(
                LocalDateTime.of(2023,10,2,14,0,0)
            ) },
            { assertThat(todayCalendar.videos.get(1).course).isEqualTo("do it") },
            { assertThat(todayCalendar.videos.get(1).count).isEqualTo(1) },
            { assertThat(todayCalendar.videos.get(1).dueAt).isEqualTo(
                LocalDateTime.of(2023,10,2,16,0,0)
            ) },
            { assertThat(todayCalendar.assignments.get(0).course).isEqualTo("i study") },
            { assertThat(todayCalendar.assignments.get(0).count).isEqualTo(2) },
            { assertThat(todayCalendar.assignments.get(0).dueAt).isEqualTo(
                LocalDateTime.of(2023,10,2,14,0,0)
            ) }
        )
        
    }
}
