package com.example.domain.course.controller

import com.example.common.dto.Response
import com.example.domain.course.dto.response.CourseResponse
import com.example.domain.course.service.CourseService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/daitssu/course")
class CourseController(
    private val courseService: CourseService
) {
    @PostMapping("")
    fun getDepartment(
        @RequestParam id: Long,
        @RequestParam userId: Long
    ): Response<CourseResponse> =
        Response(data = courseService.getCourse(courseId = id, userId = userId))
}
