package com.example.test.domain.course.controller

import com.example.test.domain.course.controller.request.RequestAssignment
import com.example.test.domain.course.controller.request.RequestCalendar
import com.example.test.domain.course.controller.request.RequestVideo
import com.example.test.domain.course.controller.response.CalendarResponse
import com.example.test.domain.course.controller.response.CourseResponse
import com.example.test.domain.course.entity.Course
import com.example.test.domain.course.service.CourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/course")
@Tag(name = "Course", description = "강의, 일정 API")
class CourseController (
    private val courseService: CourseService,
) {
    @Operation(
        summary = "강의 리스트형식 출력",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ],
    )
    @GetMapping()
    fun getCourseList() : List<Course> = courseService.getCourseList()
    
    
    @Operation(
        summary = "강의 단일 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("{courseId}")
    fun getCourse(
        @PathVariable
        courseId: Long
    ) : CourseResponse = courseService.getCourse(
        courseId = courseId,
    )
    
    
    @Operation(
        summary = "월 단위로 일정 가져오기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/calendar/{date}")
    fun getCalendar(
        @PathVariable("date")
        date: String
    ) : MutableMap<String, List<CalendarResponse>> = courseService.getCalendar(
        requestDate = date
    )
    
    
    @Operation(
        summary = "일정 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/calendar")
    fun postCreateCalendar(
        @RequestBody
        requestCalendar: RequestCalendar
    ) : Boolean = courseService.postCalendar(
        requestType =  requestCalendar.type,
        course = requestCalendar.course,
        dueAt = requestCalendar.dueAt,
        name = requestCalendar.name
    )
    
    
    @Operation(
        summary = "강의 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/video")
    fun postCreateVideo(
        @RequestBody
        requestVideo: RequestVideo
    ) : Boolean = courseService.postVideo(
        courseId = requestVideo.courseId,
        name = requestVideo.name,
    )
    
    
    @Operation(
        summary = "과제 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/assignment")
    fun postCreateAssignment(
        @RequestBody
        requestAssignemnt: RequestAssignment
    ) : Boolean = courseService.postAssignment(
        courseId = requestAssignemnt.courseId,
        name = requestAssignemnt.name,
    )
}