package com.example.daitssuapi.domain.myPage.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.article.dto.response.CommentResponse
import com.example.daitssuapi.domain.article.model.repository.ArticleRepository
import com.example.daitssuapi.domain.article.model.repository.CommentRepository
import com.example.daitssuapi.domain.article.model.repository.ScrapRepository
import com.example.daitssuapi.domain.course.model.repository.CourseRepository
import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.domain.main.dto.response.ServiceNoticeResponse
import com.example.daitssuapi.domain.main.model.repository.ServiceNoticeRepository
import com.example.daitssuapi.domain.myPage.dto.response.*
import com.example.daitssuapi.domain.user.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MyPageService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val scrapRepository: ScrapRepository,
    private val courseRepository: CourseRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository,
    private val serviceNoticeRepository: ServiceNoticeRepository,
) {
    fun getComments(userId: Long): List<CommentResponse> {
        userRepository.findByIdOrNull(id = userId) ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)

        return commentRepository.findByWriterIdOrderByIdDesc(userId = userId).map {
            CommentResponse.of(it)
        }
    }

    @Transactional
    fun deleteComments(commentIds: List<Long>) {
        commentRepository.findAllById(commentIds).forEach {
            it.isDeleted = true
        }
    }

    fun getMyArticle(userId: Long): List<MyArticleResponse> {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        return articleRepository.findAllByWriterOrderByCreatedAtDesc(user).map {
            MyArticleResponse(
                id = it.id,
                topic = it.topic.value,
                title = it.title,
                content = it.content,
                createdAt = it.createdAt,
                commentSize = it.comments.count { comment -> !comment.isDeleted }
            )
        }
    }

    fun getMyScrap(userId: Long): List<MyScrapResponse> {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        return scrapRepository.findByUserAndIsActiveTrueOrderByCreatedAtDesc(user).map {
            MyScrapResponse(
                id = it.article.id,
                topic = it.article.topic.value,
                title = it.article.title,
                content = it.article.content,
                createdAt = it.article.createdAt,
                commentSize = it.article.comments.count { comment -> !comment.isDeleted }
            )
        }
    }

    fun getAssignments(userId: Long, courseId: Long? = null): List<MyAssignmentResponse> {
        userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)

        val courses = courseId?.let {
            listOf(courseRepository.findByIdOrNull(courseId)
                ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND))
        } ?: userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(userId = userId).map { it.course }

        return courses.flatMap { course ->
            val courseResponse = MyCourseSimpleResponse(
                id = course.id,
                name = course.name,
                term = course.term,
                courseCode = course.courseCode
            )

            course.assignments.map { assignment ->
                MyAssignmentResponse(
                    id = assignment.id,
                    course = courseResponse,
                    name = assignment.name,
                    dueAt = assignment.dueAt,
                    startAt = assignment.startAt,
                    submitAt = assignment.submitAt,
                    detail = assignment.detail,
                    comments = assignment.comments
                )
            }
        }
    }

    fun getCourseNotices(userId: Long, courseId: Long): List<MyCourseNoticeResponse> {
        userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)

        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw DefaultException(errorCode = ErrorCode.COURSE_NOT_FOUND)

        val courseResponse = MyCourseSimpleResponse(
            id = course.id,
            name = course.name,
            term = course.term,
            courseCode = course.courseCode
        )

        return course.courseNotices.map { courseNotice ->
            MyCourseNoticeResponse(
                id = courseNotice.id,
                course = courseResponse,
                name = courseNotice.name,
                isActive = courseNotice.isActive,
                registeredAt = courseNotice.registeredAt,
                views = courseNotice.views,
                content = courseNotice.content,
                fileUrl = courseNotice.fileUrl,
            )
        }
    }

    fun getServiceNotice():List<ServiceNoticeResponse>{
        return serviceNoticeRepository.findAll().map { serviceNotice ->
            ServiceNoticeResponse(
                title = serviceNotice.title,
                content = serviceNotice.content,
                createdAt = serviceNotice.createdAt
            )
        }
    }
}
