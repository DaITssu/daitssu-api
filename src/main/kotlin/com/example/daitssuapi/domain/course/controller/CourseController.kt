package com.example.daitssuapi.domain.course.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.course.dto.request.AssignmentCreateRequest
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
    private val argumentResolver: ArgumentResolver,
) {
    @Operation(
        summary = "강의 리스트 형식 출력",
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
    @GetMapping("/{courseId}")
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
    @GetMapping("/calendar/{date}") // TODO : calendar에 저장된 course로 group by, 실제 강의와 연결 불가
    fun getCalendar(
        @PathVariable("date") date: String
    ): Response<Map<String, List<CalendarResponse>>> =
        Response(data = courseService.getCalendar(dateRequest = date))

    @Operation(
        summary = "일정 추가",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping("/calendar") // TODO : course를 String으로 박는데, 실제 강의와 연결을 못 시키고 있음. courseId 등으로 대체 필요
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
    @PostMapping("/video") // TODO : due가 강제로 7일 이후로 처리되는거 수정
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
    fun createAssignment(
        @RequestBody assignmentCreateRequest: AssignmentCreateRequest
    ): Response<AssignmentResponse> =
        Response(data = courseService.postAssignment(request = assignmentCreateRequest))

    @Operation(
        summary = "과목 추가",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PostMapping
    fun postCreateCourse(
        @RequestBody courseRequest: CourseRequest
    ): Response<CourseResponse> =
        Response(data = courseService.postCourse(courseRequest = courseRequest))

    @Operation(
        summary = "유저의 강의 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/user")
    fun getUserCourse(): Response<List<UserCourseResponse>> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = courseService.getUserCourses(userId = userId))
    }


    @Operation(
        summary = "강의 일정 수정",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @PutMapping("/calendar/{calendarId}") // TODO : 이거도 동일하게 강의와 Mapping 불가
    fun updateCalendar(
        @RequestBody calendarRequest: CalendarRequest,
        @PathVariable calendarId: Long
    ): Response<CalendarResponse> =
        Response(data = courseService.updateCalendar(calendarRequest = calendarRequest, calendarId = calendarId))

    @Operation(
        summary = "금일 강의 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "OK")
        ]
    )
    @GetMapping("/calendar/today")
    fun getTodayCalendar(): Response<TodayCalendarResponse> =
        Response(data = courseService.getTodayDueAtCalendars())

}
