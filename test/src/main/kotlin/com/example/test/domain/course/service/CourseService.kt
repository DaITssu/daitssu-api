package com.example.test.domain.course.service

import com.example.test.domain.course.controller.response.CalendarResponse
import com.example.test.domain.course.controller.response.CourseResponse
import com.example.test.domain.course.dto.AssignmentDto
import com.example.test.domain.course.dto.VideoDto
import com.example.test.domain.course.entity.*
import com.example.test.domain.course.repository.AssignmentRepository
import com.example.test.domain.course.repository.CalendarRepository
import com.example.test.domain.course.repository.CourseRepository
import com.example.test.domain.course.repository.VideoRepository
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
    fun getCourseList() : List<Course>{
        return courseRepository.findAll()
    }
    
    
    fun getCourse(
        courseId: Long
    ) : CourseResponse {
        val course = courseRepository.findById(courseId)
            .orElseThrow { IllegalArgumentException("not found course") }
        val courseResponse = CourseResponse(course.name)
        

        for (v in course.videos) {
            val video = VideoDto(v.dueAt, v.startAt, v.name, v.id)
            courseResponse.videos.add(video)
        }

        for (a in course.assignments) {
            val assignment = AssignmentDto(a.dueAt, a.startAt, a.name, a.id)
            courseResponse.assignments.add(assignment)
        }
        
        return courseResponse
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
        courseId: Long,
        name: String
    ) : Boolean {
        val course = courseRepository.findById(courseId)
            .orElseThrow { IllegalArgumentException("not found course") }
        val vd = Video(LocalDateTime.now().plusDays(7), LocalDateTime.now(), name, course)
        course.addVideo(vd)
        videoRepository.save(vd)
        
        return true
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
}