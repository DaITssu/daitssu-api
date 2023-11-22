package com.example.daitssuapi.domain.course.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.course.dto.request.AssignmentRequest
import com.example.daitssuapi.domain.course.dto.request.CalendarRequest
import com.example.daitssuapi.domain.course.dto.request.CourseRequest
import com.example.daitssuapi.domain.course.dto.request.VideoRequest
import com.example.daitssuapi.domain.course.dto.response.*
import com.example.daitssuapi.domain.course.service.CourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course")
@Tag(name = "Course", description = "강의, 일정 API")
class CourseController(
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
    @GetMapping
    fun getCourseList(): Response<List<CourseResponse>> =
        Response(data = courseService.getCourseList())

    @Operation(
        summary = "강의 단일 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("{courseId}")
    fun getCourse(
        @PathVariable courseId: Long
    ): Response<CourseResponse> =
        Response(data = courseService.getCourse(courseId = courseId))

    @Operation(
        summary = "월 단위 일정 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/calendar/{date}/{userId}")
    fun getCalendar(
        @PathVariable("date") date: String,
        @PathVariable("userId") userId: Long
    ): Response<Map<String, List<CalendarResponse>>> =
        Response(data = courseService.getCalendar(dateRequest = date, userId = userId))

    @Operation(
        summary = "일정 추가",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/calendar")
    fun postCreateCalendar(
        @RequestBody calendarRequest: CalendarRequest
    ): Response<CalendarResponse> =
        Response(data = courseService.postCalendar(calendarRequest = calendarRequest))

    @Operation(
        summary = "영상 추가",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/video")
    fun postCreateVideo(
        @RequestBody videoRequest: VideoRequest
    ): Response<VideoResponse> =
        Response(data = courseService.postVideo(videoRequest))

    @Operation(
        summary = "과제 추가",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/assignment")
    fun postCreateAssignment(
        @RequestBody assignmentRequest: AssignmentRequest
    ): Response<AssignmentResponse> =
        Response(data = courseService.postAssignment(assignmentRequest = assignmentRequest))

    @Operation(
        summary = "과목 추가",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/course")
    fun postCreateCourse(
        @RequestBody courseRequest: CourseRequest
    ): Response<CourseResponse> =
        Response(data = courseService.postCourse(courseRequest = courseRequest))

    //TODO : 유저 토큰 기능 구현 후 토큰에서 userId 가져오도록 변경
    @Operation(
        summary = "유저의 강의 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/user/{userId}")
    fun getUserCourse(
        @PathVariable userId: Long
    ): Response<List<UserCourseResponse>> =
        Response(data = courseService.getUserCourses(userId = userId))

    @Operation(
        summary = "강의 일정 수정",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PutMapping("/calendar/{calendarId}")
    fun updateCalendar(
        @RequestBody calendarRequest: CalendarRequest,
        @PathVariable calendarId: Long
    ) : Response<CalendarResponse> =
        Response(data = courseService.updateCalendar(calendarRequest = calendarRequest, calendarId = calendarId))

    @Operation(
        summary = "금일 강의 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/calendar/today")
    fun getTodayCalendar() : Response<TodayCalendarResponse> =
        Response(data = courseService.getTodayDueAtCalendars())

}
