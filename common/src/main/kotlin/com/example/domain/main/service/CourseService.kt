package com.example.domain.main.service

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
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
        val course = courseRepository.findById(courseId)
            .orElseThrow { IllegalArgumentException("not found course") }
        
        
        return CourseResponse(course.name, course.videos, course.assignments, course.term)
    }
    
    
    fun getCalendar(requestDate: String): MutableMap<String, List<CalendarResponse>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date: LocalDateTime
        try {
            date = LocalDateTime.parse(requestDate, formatter)
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
        
        calendarRepository.save(calendar)
        return CalendarResponse(type = calendar.type, dueAt = calendar.dueAt, name = calendar.name)
    }
    
    
    fun postVideo(
        videoRequest: VideoRequest
    ) : VideoResponse {
        val course = courseRepository.findById(videoRequest.courseId).get()
        val video = Video(LocalDateTime.now().plusDays(7), LocalDateTime.now(), videoRequest.name)
        course.addVideo(video)
        videoRepository.save(video)
        
        return VideoResponse(id = video.id, name = video.name, dueAt = video.dueAt, startAt = video.startAt)
    }
    
    fun postAssignment(
        assignmentRequest: AssignmentRequest
    ) : AssignmentResponse {
        val course = courseRepository.findById(assignmentRequest.courseId)
            .orElseThrow { IllegalArgumentException("not found course") }
        val assignment = Assignment(LocalDateTime.now().plusDays(7), LocalDateTime.now(), assignmentRequest.name, course)
        course.addAssignment(assignment)
        assignmentRepository.save(assignment)
        
        return AssignmentResponse(id = assignment.id, name = assignment.name, dueAt = assignment.dueAt, startAt = assignment.startAt)
    }
    
    fun postCourse(courseRequest: CourseRequest) : CourseResponse {
        println(courseRequest)
        val course = Course(courseRequest.name, courseRequest.term)
        courseRepository.save(course)
        
        return CourseResponse(name = course.name, videos = course.videos, assignments = course.assignments, term = course.term)
    }
}