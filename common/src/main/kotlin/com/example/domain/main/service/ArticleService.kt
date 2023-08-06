package com.example.domain.main.service

import com.example.common.enums.ErrorCode
import com.example.common.exception.DefaultException
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse
import com.example.domain.main.model.entity.Article
import com.example.domain.main.model.repository.ArticleRepository
import com.example.domain.main.model.entity.User
import com.example.domain.main.model.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun getArticle(id: Long): ArticleResponse {
        val article: Article = articleRepository.findByIdOrNull(id)
            ?: throw DefaultException(ErrorCode.BAD_REQUEST)

        return ArticleResponse(
            id = article.id,
            title = article.title,
            content = article.content,
            writerNickName = article.writer.nickname!!,
            updatedAt = article.updatedAt
        )
    }

    @Transactional
    fun writeArticle(articlePostRequest: ArticlePostRequest): ArticleResponse {
        val user: User = userRepository.findByNickname(articlePostRequest.nickname)
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
            writerNickName = user.nickname!!,
            updatedAt = savedArticle.updatedAt
        )
    }
}