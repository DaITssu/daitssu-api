package com.example.domain.main.service

import com.example.common.enums.CalendarCourseType
import com.example.domain.main.dto.response.CalendarResponse
import com.example.domain.main.dto.response.CourseResponse
import com.example.domain.main.dto.request.RequestCourse
import com.example.domain.main.dto.request.RequestVideo
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
            CourseResponse(course.name, course.videos, course.assignments, course.term)
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
    
    
    fun postCalendar(
        requestType: CalendarCourseType,
        course: String,
        dueAt: String,
        name: String) : Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime:LocalDateTime
        try {
            dateTime = LocalDateTime.parse(dueAt, formatter)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format. Date should be in 'yyyy-MM-dd HH:mm:ss' format.")
        }
        val cal = Calendar(requestType, course, dateTime, name)
        
        calendarRepository.save(cal)
        return true
    }
    
    
    fun postVideo(
        requestVideo: RequestVideo
    ) : VideoResponse {
        val course = courseRepository.findById(requestVideo.courseId).get()
        val video = Video(LocalDateTime.now().plusDays(7), LocalDateTime.now(), requestVideo.name)
        course.addVideo(video)
        videoRepository.save(video)
        
        return VideoResponse(id = video.id, name = video.name, dueAt = video.dueAt, startAt = video.startAt)
    }
    
    fun postAssignment(
        courseId: Long,
        name: String,
    ) : Boolean {
        val course = courseRepository.findById(courseId)
            .orElseThrow { IllegalArgumentException("not found course") }
        val assignment = Assignment(LocalDateTime.now().plusDays(7), LocalDateTime.now(), name, course)
        course.addAssignment(assignment)
        assignmentRepository.save(assignment)
        
        return true
    }
    
    fun postCourse(requestCourse: RequestCourse) : Boolean {
        println(requestCourse)
        val course = Course(requestCourse.name, requestCourse.term)
        courseRepository.save(course)
        return true
    }
}