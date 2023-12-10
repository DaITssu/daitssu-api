package com.example.daitssuapi.domain.user.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.user.dto.response.UserResponse
import com.example.daitssuapi.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "유저 API")
class UserController(
    private val userService: UserService,
    private val argumentResolver: ArgumentResolver,
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
    @GetMapping
    fun getUser(): Response<UserResponse> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = userService.getUser(userId = userId))
    }

    @Operation(
        summary = "유저 닉네임 수정",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PatchMapping("/nickname")
    fun updateNickname(
        @RequestParam nickname: String,
    ): Response<String> {
        val userId = argumentResolver.resolveUserId()

        return Response(data = userService.updateNickname(userId = userId, nickname = nickname))
    }

    @Operation(
        summary = "유저 프로필 수정",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PatchMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], value = ["/image"])
    fun updateUserProfile(
        @RequestPart("profileImage") profileImage: MultipartFile
    ): Response<String> {
        val userId = argumentResolver.resolveUserId()

        userService.updateProfileImage(userId = userId, image = profileImage)
        return Response(code = 0, message = "OK", data = null)
    }

}
