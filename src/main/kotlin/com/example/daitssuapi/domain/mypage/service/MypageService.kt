package com.example.daitssuapi.domain.mypage.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.ScrapRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.mypage.dto.response.MyArticleResponse
import com.example.daitssuapi.domain.mypage.dto.response.MyScrapResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MypageService (
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val scrapRepository: ScrapRepository
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
    
    fun getMyScrap(userId: Long) : List<MyScrapResponse> {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        return scrapRepository.findByUserAndIsActiveTrue(user).map {
            MyScrapResponse(
                id = it.article.id,
                topic = it.article.topic.value,
                title = it.article.title,
                content = it.article.content,
                createdAt = it.article.createdAt,
                commentSize = commentRepository.findByArticleId(it.article.id).size
            )
        }
    }

}