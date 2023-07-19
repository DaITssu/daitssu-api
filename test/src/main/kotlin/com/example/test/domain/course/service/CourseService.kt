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

@Service
class CourseService (
    private val assignmentRepository: AssignmentRepository,
    private val courseRepository: CourseRepository,
    private val videoRepository: VideoRepository,
    private val calendarRepository: CalendarRepository,
) {
    fun getCourseList() : List<Course>{
        val courseResponse: MutableList<Course> = mutableListOf()
        val course = courseRepository.findAll()
        for (c in course) {
            courseResponse.add(c)
        }
        return courseResponse
    }
    
    
    fun getCourse(
        courseId: Long
    ) : CourseResponse {
        val course = courseRepository.findById(courseId)
        val videos: List<Video> = videoRepository.findByCourseId(courseId)
        val assignments: List<Assignment> = assignmentRepository.findByCourseId(courseId)
        val courseResponse = CourseResponse(course.get().name)
        
        for (v in videos) {
            val vd = VideoDto(v.dueAt, v.startAt, v.name, v.id)
            courseResponse.videos.add(vd)
        }
        
        for (a in assignments) {
            val am = AssignmentDto(a.dueAt, a.startAt, a.name, a.id)
            courseResponse.assignments.add(am)
        }
        
        return courseResponse
    }
    
    
    fun getCalendar(date: String): MutableMap<String, List<CalendarResponse>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.parse(date, formatter)
        val yearMonth = YearMonth.of(date.year, date.monthValue)
        val startDateTime = yearMonth.atDay(1).atStartOfDay()
        val endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59)
        val calendars: List<Calendar> = calendarRepository.findByDueAtBetween(startDateTime, endDateTime)
        val calendarResponse: MutableList<CalendarResponse> = mutableListOf()
        
        val groupedCalendars = calendars.groupBy { it.course }
        val resultMap = mutableMapOf<String, List<CalendarResponse>>()
        
        for ((course, calendarList) in groupedCalendars) {
            val gcrList = calendarList.map {
                CalendarResponse(it.type, it.dueAt.toString(), it.name)
            }
            resultMap[course] = gcrList
        }
        
        return resultMap
        
//        for ( c in calendars ) {
//            val hour = c.dueAt.hour
//            val minute = c.dueAt.minute
//
//            val timeString = String.format("%02d:%02d", hour, minute)
//            val formattedDate = "${timeString}까지"
//            val gcl = GetCalendarResponse(c.type, c.course, formattedDate, c.name)
//
//           getCalendarResponse.add(gcl)
//        }
//
//        return getCalendarResponse
    }
    
    
    fun postCalendar(
        requestType: String,
        course: String,
        dueAt: String,
        name: String) : Boolean {
        val type = when (requestType) {
            "과제" -> CalendarCourseType.ASSIGNMENT
            "강의" -> CalendarCourseType.VIDEO
            else -> throw Exception("No Course Type")
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dueAt, formatter)
        val cal = Calendar(type, course, dateTime, name)
        
        calendarRepository.save(cal)
        return true
    }
    
    
    fun postVideo(
        courseId: Long,
        name: String
    ) : Boolean {
        val course = courseRepository.findById(courseId).get()
        val vd = Video(LocalDateTime.now().plusDays(7), LocalDateTime.now(), name, course)
        course.addVideo(vd)
        videoRepository.save(vd)
        
        return true
    }
    
    fun postAssignment(
        courseId: Long,
        name: String,
    ) : Boolean {
        val course = courseRepository.findById(courseId).get()
        val am = Assignment(LocalDateTime.now().plusDays(7), LocalDateTime.now(), name, course)
        course.addAssignment(am)
        assignmentRepository.save(am)
        
        return true
    }
}