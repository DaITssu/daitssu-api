package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.domain.main.dto.request.ArticlePostRequest
import com.example.domain.main.dto.response.ArticleResponse

import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.main.enums.Topic
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun getArticle(id: Long): ArticleResponse {
        val article: Article = articleRepository.findByIdOrNull(id)
            ?: throw DefaultException(ErrorCode.BAD_REQUEST, HttpStatus.NOT_FOUND)

        return ArticleResponse(
            id = article.id,
            topic = article.topic.value,
            title = article.title,
            content = article.content,
            writerNickName = article.writer.nickname!!,
            updatedAt = article.updatedAt
        )
    }

    @Transactional
    fun writeArticle(articlePostRequest: ArticlePostRequest): ArticleResponse {
        if (articlePostRequest.nickname == null
            || Topic[articlePostRequest.topic] == null) {
            throw DefaultException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST)
        }

        val user: User = userRepository.findByNickname(articlePostRequest.nickname)
            ?: throw DefaultException(ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST)

        val article: Article = Article(
            topic = Topic[articlePostRequest.topic]!!,
            title = articlePostRequest.title,
            content = articlePostRequest.content,
            writer = user
        )

        val savedArticle = articleRepository.save(article)

        return ArticleResponse(
            id = savedArticle.id,
            topic = savedArticle.topic.value,
            title = savedArticle.title,
            content = savedArticle.content,
            writerNickName = user.nickname!!,
            updatedAt = savedArticle.updatedAt
        )
    }
}