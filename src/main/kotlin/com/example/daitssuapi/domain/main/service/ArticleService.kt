package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {
    fun getArticle(id: Long): ArticleResponse {
        val article: Article = articleRepository.findByIdOrNull(id)
            ?: throw DefaultException(ErrorCode.BAD_REQUEST)

        return ArticleResponse(
            id = article.id,
            title = article.title,
            content = article.content,
            writer = article.writer,
            updatedAt = article.updatedAt
        )
    }

    fun writeArticle(articlePostRequest: ArticlePostRequest): ArticleResponse {
        val user: User = userRepository.findByStudentId(articlePostRequest.studentId)
            ?: throw DefaultException(ErrorCode.USER_NOT_FOUND)

        val article: Article = Article(
            title = articlePostRequest.title,
            content = articlePostRequest.content,
            writer = user
        )

        val savedArticle = articleRepository.save(article)

        return ArticleResponse(
            id = savedArticle.id,
            title = savedArticle.title,
            content = savedArticle.content,
            writer = savedArticle.writer,
            updatedAt = savedArticle.updatedAt
        )
    }
}