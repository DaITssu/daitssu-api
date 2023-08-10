package com.example.domain.course.controller

import com.example.common.dto.Response
import com.example.domain.course.dto.response.CourseResponse
import com.example.domain.course.service.CourseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/daitssu/course")
class CourseController(
    private val courseService: CourseService
) {
    @GetMapping("/{userId}")
    fun getDepartment(
        @PathVariable userId: Long
    ): Response<List<CourseResponse>> =
        Response(data = courseService.getCourse(userId = userId))
}
