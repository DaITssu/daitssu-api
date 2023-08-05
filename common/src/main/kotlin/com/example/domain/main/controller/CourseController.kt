package com.example.domain.main.controller

import com.example.domain.main.dto.request.AssignmentRequest
import com.example.domain.main.dto.request.CalendarRequest
import com.example.domain.main.dto.request.CourseRequest
import com.example.domain.main.dto.request.VideoRequest
import com.example.domain.main.dto.response.AssignmentResponse
import com.example.domain.main.dto.response.CalendarResponse
import com.example.domain.main.dto.response.CourseResponse
import com.example.domain.main.dto.response.VideoResponse
import com.example.domain.main.service.CourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
    fun getCourseList() : List<CourseResponse> = courseService.getCourseList()
    
    
    @Operation(
        summary = "강의 단일 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("{courseId}")
    fun getCourse(
        @PathVariable courseId: Long
    ) : CourseResponse = courseService.getCourse(courseId = courseId)
    
    
    @Operation(
        summary = "월 단위로 일정 가져오기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/calendar/{date}")
    fun getCalendar(
        @PathVariable("date") date: String
    ) : MutableMap<String, List<CalendarResponse>> = courseService.getCalendar(requestDate = date)
    
    
    @Operation(
        summary = "일정 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/calendar")
    fun postCreateCalendar(
        @RequestBody calendarRequest: CalendarRequest
    ) : Boolean = courseService.postCalendar(calendarRequest = calendarRequest)
    
    
    @Operation(
        summary = "강의 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/video")
    fun postCreateVideo(
        @RequestBody videoRequest: VideoRequest
    ) : VideoResponse = courseService.postVideo(videoRequest)
    
    
    @Operation(
        summary = "과제 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/assignment")
    fun postCreateAssignment(
        @RequestBody requestAssignemnt: AssignmentRequest
    ) : AssignmentResponse = courseService.postAssignment(assignmentRequest = requestAssignemnt)
    
    
    @Operation(
        summary = "과목 추가하기",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/course")
    fun postCreateCourse(
        @RequestBody courseRequest: CourseRequest
    ) : CourseResponse = courseService.postCourse(courseRequest = courseRequest)
}