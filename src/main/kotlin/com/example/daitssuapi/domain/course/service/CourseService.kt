package com.example.daitssuapi.domain.course.service

import com.example.daitssuapi.common.enums.CalendarType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.course.dto.request.*
import com.example.daitssuapi.domain.course.dto.response.*
import com.example.daitssuapi.domain.course.dto.response.CourseNoticeResponse
import com.example.daitssuapi.domain.course.model.entity.*
import com.example.daitssuapi.domain.course.model.repository.*
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
    private val videoRepository: VideoRepository,
    private val courseRepository: CourseRepository,
    private val calendarRepository: CalendarRepository,
    private val assignmentRepository: AssignmentRepository,
    private val courseNoticeRepository: CourseNoticeRepository,
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

    fun getCalendar(dateRequest: String, userId: Long): List<CalendarsResponse> {
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
        
        
        val calendars = calendarRepository.findByUserIdAndDueAtBetween(userId, startDateTime, endDateTime).groupBy(
            { it.course.name }, { CalendarResponse(it.id, it.type, it.dueAt, it.name, it.isCompleted) }
        )
        
        return calendars.map { (course, calendars) ->
            CalendarsResponse(course = course, calendarResponses = calendars)
        }
    }

    fun postCalendar(calendarRequest: CalendarRequest, userId: Long): CalendarResponse {
        val dateTime = checkDateReturnDate(calendarRequest.dueAt)

        val course = courseRepository.findByName(calendarRequest.course)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)

        val calendar = Calendar(
            type = calendarRequest.type,
            course = course,
            dueAt = dateTime,
            name = calendarRequest.name,
            isCompleted = calendarRequest.isCompleted,
            userId = userId
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
        
        val course = courseRepository.findByName(calendarRequest.course)
            ?: throw DefaultException(ErrorCode.COURSE_NOT_FOUND)

        calendar.updateCalendar(calendarRequest = calendarRequest, dueAt = dateTime, course = course)
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
    
    fun getTodayDueAtCalendars(userId: Long) : TodayCalendarResponse {
        val day = LocalDate.now()
        val startTime = LocalTime.of(0, 0, 0)
        val endTime = LocalTime.of(23, 59, 59)
        val todayStart = checkDateReturnDate("$day $startTime:00")
        val todayEnd = checkDateReturnDate("$day $endTime")
        val videos: MutableList<TodayCalendarDataDto> = mutableListOf()
        val assignments: MutableList<TodayCalendarDataDto> = mutableListOf()
        
        val videoCourses = calendarRepository.findDistinctTop2ByUserIdAndTypeAndDueAtBetweenOrderByDueAtAsc(
            startDateTime = todayStart,
            endDateTime = todayEnd,
            type = CalendarType.VIDEO,
            userId = userId
        )
        
        val assignmentCourses = calendarRepository.findDistinctTop2ByUserIdAndTypeAndDueAtBetweenOrderByDueAtAsc(
            startDateTime = todayStart,
            endDateTime = todayEnd,
            type = CalendarType.ASSIGNMENT,
            userId = userId
        )

        for (calendar: Calendar in videoCourses) {
            val calendars = calendarRepository.findByUserIdAndTypeAndCourseAndDueAtBetween(
                type = CalendarType.VIDEO,
                course = calendar.course,
                startDateTime = todayStart,
                endDateTime = todayEnd,
                userId = userId
            )

            val todayCalendarDataDto = TodayCalendarDataDto(
                course = calendar.course.name,
                dueAt = calendars.minOf { it.dueAt },
                count = calendars.size
            )

            videos.add(todayCalendarDataDto)
        }

        for (calendar: Calendar in assignmentCourses) {
            val calendars = calendarRepository.findByUserIdAndTypeAndCourseAndDueAtBetween(
                type = CalendarType.ASSIGNMENT,
                course = calendar.course,
                startDateTime = todayStart,
                endDateTime = todayEnd,
                userId = userId
            )

            val todayCalendarDataDto = TodayCalendarDataDto(
                course = calendar.course.name,
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

    fun getNotices(courseId: Long): List<CourseNoticeResponse> =
        courseNoticeRepository.findByCourseId(courseId = courseId).map(CourseNoticeResponse::of)

    fun getNotice(courseId: Long, noticeId: Long): CourseNoticeResponse {
        val courseNotice = courseNoticeRepository.findByIdOrNull(id = noticeId)?.also { it.viewNotice() }
            ?: throw DefaultException(errorCode = ErrorCode.NOTICE_NOT_FOUND)

        return CourseNoticeResponse.of(courseNotice)
    }

    @Transactional
    fun createNotice(request: CourseNoticeCreateRequest): CourseNoticeResponse {
        val course = courseRepository.findByIdOrNull(id = request.courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)

        val courseNotice = CourseNotice(
            name = request.name,
            registeredAt = request.registeredAt,
            content = request.content,
            fileUrl = request.fileUrl ?: emptyList(),
            course = course
        ).also { courseNoticeRepository.save(it) }

        return CourseNoticeResponse.of(courseNotice)
    }

    @Transactional
    fun updateNotice(noticeId: Long, request: CourseNoticeUpdateRequest): CourseNoticeResponse {
        val courseNotice = courseNoticeRepository.findByIdOrNull(id = noticeId)?.also {
            it.update(
                content = request.content,
                fileUrl = request.fileUrl
            )
        } ?: throw DefaultException(errorCode = ErrorCode.NOTICE_NOT_FOUND)

        return CourseNoticeResponse.of(courseNotice)
    }
}

