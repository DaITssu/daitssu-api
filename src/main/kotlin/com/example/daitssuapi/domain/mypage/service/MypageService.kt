package com.example.daitssuapi.domain.mypage.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.mypage.dto.response.MyArticleResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MypageService (
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
){
    
    fun getMyArticle(userId: Long) : List<MyArticleResponse> {
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
    

}