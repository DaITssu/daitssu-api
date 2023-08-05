package com.example.domain.main.service

import com.example.common.enums.ErrorCode
import com.example.common.exception.DefaultException
import com.example.domain.main.dto.request.AssignmentRequest
import com.example.domain.main.dto.request.CalendarRequest
import com.example.domain.main.dto.response.CalendarResponse
import com.example.domain.main.dto.response.CourseResponse
import com.example.domain.main.dto.request.CourseRequest
import com.example.domain.main.dto.request.VideoRequest
import com.example.domain.main.dto.response.AssignmentResponse
import com.example.domain.main.dto.response.VideoResponse
import com.example.domain.main.model.respository.AssignmentRepository
import com.example.domain.main.model.respository.CalendarRepository
import com.example.domain.main.model.respository.CourseRepository
import com.example.domain.main.model.respository.VideoRepository
import com.example.domain.main.model.entity.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.expression.spel.ast.Assign
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAdjusters
import kotlin.jvm.optionals.getOrNull

@Service
class CourseService (
    private val assignmentRepository: AssignmentRepository,
    private val courseRepository: CourseRepository,
    private val videoRepository: VideoRepository,
    private val calendarRepository: CalendarRepository,
) {
    fun getCourseList(): List<CourseResponse> {
        val courses: List<Course> = courseRepository.findAll()
        return courses.map { course ->
            CourseResponse(name = course.name, term = course.term)
        }
    }
    
    
    fun getCourse(
        courseId: Long
    ): CourseResponse {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)
        
        val videoResponses = course.videos.map { video ->
            VideoResponse(id = video.id, name = video.name, dueAt = video.dueAt, startAt = video.startAt)
        }
        
        val assignmentResponses = course.assignments.map { assignment ->
            AssignmentResponse(id = assignment.id , name = assignment.name, dueAt = assignment.dueAt, startAt = assignment.startAt)
        }
        
        return CourseResponse(name = course.name, videos = videoResponses, assignments = assignmentResponses, term = course.term)
    }
    
    
    fun getCalendar(requestDate: String): MutableMap<String, List<CalendarResponse>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date: LocalDateTime
        try {
            date = LocalDateTime.parse(requestDate, formatter)
            val lastDayOfMonth = date.toLocalDate().with(TemporalAdjusters.lastDayOfMonth())
            if (date.toLocalDate().isAfter(lastDayOfMonth)) {
                throw IllegalArgumentException("Invalid date. Date is after the last day of the month.")
            }
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format. Date should be in 'yyyy-MM-dd HH:mm:ss' format.")
        }
        
        val yearMonth = YearMonth.of(date.year, date.monthValue)
        val startDateTime = yearMonth.atDay(1).atStartOfDay()
        val endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59)
        val calendars: List<Calendar> = calendarRepository.findByDueAtBetween(startDateTime, endDateTime)
        

        val groupedCalendars = calendars.groupBy { it.course }
        val resultMap = groupedCalendars.mapValues { (_, calendarList) ->
            calendarList.map { CalendarResponse(it.type, it.dueAt, it.name) }
        }.toMutableMap()
        
        
        return resultMap
        
    }
    
    
    fun postCalendar(calendarRequest: CalendarRequest) : CalendarResponse {
        
        
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime:LocalDateTime
        try {
            dateTime = LocalDateTime.parse(calendarRequest.dueAt, formatter)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format. Date should be in 'yyyy-MM-dd HH:mm:ss' format.")
        }
        val calendar = Calendar(type = calendarRequest.type, course = calendarRequest.course, dueAt = dateTime, name = calendarRequest.name)
            .also { calendarRepository.save(it) }
        
        return CalendarResponse(type = calendar.type, dueAt = calendar.dueAt, name = calendar.name)
    }
    
    
    fun postVideo(
        videoRequest: VideoRequest
    ) : VideoResponse {
        val course = courseRepository.findByIdOrNull(videoRequest.courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)
        
        val video = Video(LocalDateTime.now().plusDays(7), LocalDateTime.now(), videoRequest.name)
            .also { videoRepository.save(it) }
        course.addVideo(video)
        
        return VideoResponse(id = video.id, name = video.name, dueAt = video.dueAt, startAt = video.startAt)
    }
    
    fun postAssignment(
        assignmentRequest: AssignmentRequest
    ) : AssignmentResponse {
        val course = courseRepository.findByIdOrNull(assignmentRequest.courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)
        
        
        val assignment = Assignment(LocalDateTime.now().plusDays(7), LocalDateTime.now(), assignmentRequest.name, course)
            .also { assignmentRepository.save(it) }
        course.addAssignment(assignment)
        
        return AssignmentResponse(id = assignment.id, name = assignment.name, dueAt = assignment.dueAt, startAt = assignment.startAt)
    }
    
    fun postCourse(courseRequest: CourseRequest) : CourseResponse {
        val course = Course(courseRequest.name, courseRequest.term)
            .also { courseRepository.save(it) }
        
        return CourseResponse(name = course.name, term = course.term)
    }
}