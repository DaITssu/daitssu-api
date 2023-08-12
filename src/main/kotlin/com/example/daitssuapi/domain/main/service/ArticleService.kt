package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse

import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.main.dto.request.ArticleWritingRequest
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
            topic = article.topic.value,
            title = article.title,
            content = article.content,
            writerNickName = article.writer.nickname!!,
            updatedAt = article.updatedAt
        )
    }

    @Transactional
    fun writeArticle(articleWritingRequest: ArticleWritingRequest): ArticleResponse {
        if (articleWritingRequest.nickname == null) {
            throw DefaultException(ErrorCode.NO_NICKNAME)
        }

        val user: User = userRepository.findByNickname(articleWritingRequest.nickname)
            ?: throw DefaultException(ErrorCode.USER_NOT_FOUND)

        val article: Article = Article(
            topic = articleWritingRequest.topic,
            title = articleWritingRequest.title,
            content = articleWritingRequest.content,
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