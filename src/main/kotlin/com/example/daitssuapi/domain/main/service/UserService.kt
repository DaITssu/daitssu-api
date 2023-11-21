package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.DomainType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.infra.service.S3Service
import com.example.daitssuapi.domain.main.dto.response.UserResponse
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val userRepository: UserRepository,
    private val s3Service: S3Service,
) {
    fun getUser(userId: Long): UserResponse {
        val user = (userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND, httpStatus = HttpStatus.NOT_FOUND))

        return UserResponse(
            studentId = user.studentId,
            name = user.name,
            nickname = user.nickname ?: throw DefaultException(errorCode = ErrorCode.USER_NICKNAME_MISSING),
            departmentName = user.department.name,
            term = user.term,
            imageUrl = user.imageUrl
        )
    }

    @Transactional
    fun updateNickname(userId: Long, nickname:String):String?{

        var user = (userRepository.findByIdOrNull(userId)?.apply{
            this.nickname = nickname
        }?.also {
            userRepository.save(it)
        } ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND, httpStatus = HttpStatus.NOT_FOUND))

        return user.nickname
    }
    @Transactional
    fun updateProfileImage(userId:Long, image:MultipartFile){
        var user: User = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(ErrorCode.USER_NOT_FOUND)

        val imageUrl :String? = user.imageUrl

        imageUrl?.let{
            s3Service.deleteFromS3ByUrl(imageUrl)
        }

        val url = s3Service.uploadImageToS3(
            userId = userId,
            domain = DomainType.MYPAGE.name,
            fileName = image.originalFilename!!,
            imageByteArray = image.bytes,
        )

        runCatching {
            user.imageUrl = url
            userRepository.save(user)
        }.onFailure {
            s3Service.deleteFromS3ByUrl(url)
        }.getOrThrow()
    }
}
