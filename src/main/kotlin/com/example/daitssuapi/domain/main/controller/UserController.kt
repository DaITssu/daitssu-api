package com.example.daitssuapi.domain.main.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.main.dto.response.UserResponse
import com.example.daitssuapi.domain.main.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "유저 API")
class UserController(
    private val userService: UserService
) {
    @Operation(
        summary = "유저 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            ),
            ApiResponse(
                responseCode = "404",
                description = "유저 없음"
            )
        ]
    )
    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long
    ): Response<UserResponse> = Response(data = userService.getUser(userId = userId))


    @PutMapping("/nickname")
    fun updateNickname(
        @RequestParam userId: Long,
        @RequestParam nickname: String,
    ): Response<UserResponse> =
        Response(data = userService.updateNickname(userId = userId, nickname = nickname))
}
