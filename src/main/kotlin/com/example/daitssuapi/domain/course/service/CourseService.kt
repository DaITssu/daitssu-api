package com.example.daitssuapi.domain.course.service

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.course.dto.request.AssignmentCreateRequest
import com.example.daitssuapi.domain.course.dto.request.AssignmentUpdateRequest
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import com.example.daitssuapi.domain.course.dto.request.CourseRequest
import com.example.daitssuapi.domain.course.dto.request.VideoRequest
import com.example.daitssuapi.domain.course.dto.response.AssignmentResponse
import com.example.daitssuapi.domain.course.dto.response.CalendarResponse
import com.example.daitssuapi.domain.course.dto.response.CourseResponse
import com.example.daitssuapi.domain.course.dto.response.TodayCalendarDataDto
import com.example.daitssuapi.domain.course.dto.response.TodayCalendarResponse
import com.example.daitssuapi.domain.course.dto.response.UserCourseResponse
import com.example.daitssuapi.domain.course.dto.response.VideoResponse
import com.example.daitssuapi.domain.course.model.entity.Assignment
import com.example.daitssuapi.domain.course.model.entity.Calendar
import com.example.daitssuapi.domain.course.model.entity.Course
import com.example.daitssuapi.domain.course.model.entity.Video
import com.example.daitssuapi.domain.course.model.repository.AssignmentRepository
import com.example.daitssuapi.domain.course.model.repository.CalendarRepository
import com.example.daitssuapi.domain.course.model.repository.CourseRepository
import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.domain.course.model.repository.VideoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Service
class CourseService(
    private val assignmentRepository: AssignmentRepository,
    private val courseRepository: CourseRepository,
    private val videoRepository: VideoRepository,
    private val calendarRepository: CalendarRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    fun getCourseList(): List<CourseResponse> {
        val courses: List<Course> = courseRepository.findAll()
        return courses.map { course ->
            CourseResponse(name = course.name, term = course.term, id = course.id, courseCode = course.courseCode)
        }
    }

    fun getCourse(
        courseId: Long
    ): CourseResponse {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)

        val videoResponses = course.videos.map {
            VideoResponse(
                id = it.id,
                name = it.name,
                dueAt = it.dueAt,
                startAt = it.startAt
            )
        }

        val assignmentResponses = course.assignments.map {
            AssignmentResponse(
                id = it.id,
                courseId = it.course.id,
                name = it.name,
                dueAt = it.dueAt,
                startAt = it.startAt,
                submitAt = it.submitAt,
                detail = it.detail,
                comments = it.comments
            )
        }

        return CourseResponse(
            id = course.id,
            name = course.name,
            videos = videoResponses,
            assignments = assignmentResponses,
            term = course.term,
            courseCode = course.courseCode
        )
    }

    fun getCalendar(dateRequest: String): Map<String, List<CalendarResponse>> {
        val date = "$dateRequest-01"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateTime: LocalDate
        try {
            dateTime = LocalDate.parse(date, formatter)
        } catch (e: DateTimeParseException) {
            throw DefaultException(errorCode = ErrorCode.INVALID_GET_DATE_FORMAT)
        }

        val yearMonth = YearMonth.of(dateTime.year, dateTime.monthValue)
        val startDateTime = yearMonth.atDay(1).atStartOfDay()
        val endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59)

        return calendarRepository.findByDueAtBetween(startDateTime, endDateTime).groupBy(
            { it.course }, { CalendarResponse(it.id, it.type, it.dueAt, it.name, it.isCompleted) }
        )
    }

    @Transactional
    fun postCalendar(calendarRequest: CalendarRequest): CalendarResponse {
        val dateTime = checkDateReturnDate(calendarRequest.dueAt)

        val calendar = Calendar(
            type = calendarRequest.type,
            course = calendarRequest.course,
            dueAt = dateTime,
            name = calendarRequest.name,
            isCompleted = calendarRequest.isCompleted
        ).also { calendarRepository.save(it) }

        return CalendarResponse(
            id = calendar.id,
            type = calendar.type,
            dueAt = calendar.dueAt,
            name = calendar.name,
            isCompleted = calendar.isCompleted
        )
    }

    @Transactional
    fun postVideo(
        videoRequest: VideoRequest
    ): VideoResponse {
        val course = courseRepository.findByIdOrNull(videoRequest.courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)

        val video = Video(
            dueAt = LocalDateTime.now().plusDays(7),
            startAt = LocalDateTime.now(),
            name = videoRequest.name,
            course = course
        ).also { videoRepository.save(it) }

        course.addVideo(video)

        return VideoResponse(
            id = video.id,
            name = video.name,
            dueAt = video.dueAt,
            startAt = video.startAt
        )
    }

    @Transactional
    fun postAssignment(
        request: AssignmentCreateRequest
    ): AssignmentResponse {
        val course = courseRepository.findByIdOrNull(request.courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)

        val assignment = with(request) {
            Assignment(
                course = course,
                name = name,
                dueAt = dueAt,
                startAt = startAt,
                submitAt = submitAt,
                detail = detail,
                comments = comments
            )
        }.also {
            assignmentRepository.save(it)
            course.addAssignment(it)
        }

        return with(assignment) {
            AssignmentResponse(
                id = id,
                courseId = course.id,
                name = name,
                dueAt = dueAt,
                startAt = startAt,
                submitAt = submitAt,
                detail = detail,
                comments = comments
            )
        }
    }

    @Transactional
    fun updateAssignment(request: AssignmentUpdateRequest): AssignmentResponse {
        val assignment = assignmentRepository.findByIdOrNull(id = request.id)?.also {
            it.update(
                dueAt = request.dueAt,
                startAt = request.startAt,
                submitAt = request.submitAt,
                detail = request.detail,
                comments = request.comments
            )
        } ?: throw DefaultException(errorCode = ErrorCode.ASSIGNMENT_NOT_FOUND)

        return with(assignment) {
            AssignmentResponse(
                id = id,
                courseId = course.id,
                name = name,
                dueAt = dueAt,
                startAt = startAt,
                submitAt = submitAt,
                detail = detail,
                comments = comments
            )
        }
    }

    @Transactional
    fun postCourse(courseRequest: CourseRequest): CourseResponse {
        val course = Course(courseRequest.name, courseRequest.term, courseRequest.courseCode)
            .also { courseRepository.save(it) }

        return CourseResponse(name = course.name, term = course.term, id = course.id, courseCode = course.courseCode)
    }

    fun getUserCourses(userId: Long): List<UserCourseResponse> =
        userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(userId = userId).filter {
            RegisterStatus.ACTIVE == it.registerStatus
        }.map {
            UserCourseResponse(
                courseId = it.course.id,
                name = it.course.name,
                term = it.course.term,
                updatedAt = it.course.updatedAt
            )
        }

    @Transactional
    fun updateCalendar(calendarRequest: CalendarRequest, calendarId: Long): CalendarResponse {
        val calendar = calendarRepository.findByIdOrNull(calendarId)
            ?: throw DefaultException(ErrorCode.CALENDAR_NOT_FOUND)

        val dateTime = checkDateReturnDate(calendarRequest.dueAt)

        calendar.updateCalendar(calendarRequest = calendarRequest, dueAt = dateTime)
            .also { calendarRepository.save(calendar) }

        return CalendarResponse(
            id = calendar.id,
            type = calendar.type,
            dueAt = calendar.dueAt,
            name = calendar.name,
            isCompleted = calendar.isCompleted
        )
    }

    fun checkDateReturnDate(dueAt: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime: LocalDateTime
        try {
            dateTime = LocalDateTime.parse(dueAt, formatter)
        } catch (e: DateTimeParseException) {
            throw DefaultException(errorCode = ErrorCode.INVALID_DATE_FORMAT)
        }

        return dateTime
    }

    fun getTodayDueAtCalendars(): TodayCalendarResponse {
        val day = LocalDate.now()
        val startTime = LocalTime.of(0, 0, 0)
        val endTime = LocalTime.of(23, 59, 59)
        val todayStart = checkDateReturnDate("$day $startTime:00")
        val todayEnd = checkDateReturnDate("$day $endTime")
        val videos: MutableList<TodayCalendarDataDto> = mutableListOf()
        val assignments: MutableList<TodayCalendarDataDto> = mutableListOf()

        val videoCourses = calendarRepository.findDistinctTop2ByTypeAndDueAtBetweenOrderByDueAtAsc(
            startDateTime = todayStart,
            endDateTime = todayEnd,
            type = CalendarType.VIDEO
        )

        val assignmentCourses = calendarRepository.findDistinctTop2ByTypeAndDueAtBetweenOrderByDueAtAsc(
            startDateTime = todayStart,
            endDateTime = todayEnd,
            type = CalendarType.ASSIGNMENT
        )

        for (calendar: Calendar in videoCourses) {
            val calendars = calendarRepository.findByTypeAndCourseAndDueAtBetween(
                type = CalendarType.VIDEO,
                course = calendar.course,
                startDateTime = todayStart,
                endDateTime = todayEnd
            )

            val todayCalendarDataDto = TodayCalendarDataDto(
                course = calendar.course,
                dueAt = calendars.minOf { it.dueAt },
                count = calendars.size
            )

            videos.add(todayCalendarDataDto)
        }

        for (calendar: Calendar in assignmentCourses) {
            val calendars = calendarRepository.findByTypeAndCourseAndDueAtBetween(
                type = CalendarType.ASSIGNMENT,
                course = calendar.course,
                startDateTime = todayStart,
                endDateTime = todayEnd
            )

            val todayCalendarDataDto = TodayCalendarDataDto(
                course = calendar.course,
                dueAt = calendars.minOf { it.dueAt },
                count = calendars.size
            )

            assignments.add(todayCalendarDataDto)
        }

        return TodayCalendarResponse(
            videos = videos,
            assignments = assignments
        )
    }
}

