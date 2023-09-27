package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.DomainType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.infra.service.S3Service
import com.example.daitssuapi.domain.main.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.dto.response.PageArticlesResponse
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.ArticleImage
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.ArticleImageRepository
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val articleImageRepository: ArticleImageRepository,
    private val userRepository: UserRepository,
    private val s3Service: S3Service,
) {
    fun getArticle(id: Long): ArticleResponse {
        val article: Article = articleRepository.findByIdOrNull(id)
            ?: throw DefaultException(ErrorCode.ARTICLE_NOT_FOUND)

        return ArticleResponse(
            id = article.id,
            topic = article.topic.value,
            title = article.title,
            content = article.content,
            writerNickName = article.writer.nickname!!,
            updatedAt = article.updatedAt,
            imageUrls = article.articleImages.map { it.url }
        )
    }

    fun pageArticleList(
        pageable: Pageable,
        inquiry: String?,
    ): PageArticlesResponse {
        val articles: Page<Article> =
            if (inquiry == null)
                articleRepository.findAll(pageable)
            else
                articleRepository.findAllByTitleContainingOrContentContaining(
                    title = inquiry,
                    content = inquiry,
                    pageable = pageable,
                )

        val articleResponses = articles.map {
            ArticleResponse(
                id = it.id,
                topic = it.topic.value,
                title = it.title,
                content = it.content,
                writerNickName = it.writer.nickname!!,
                updatedAt = it.updatedAt,
                imageUrls = it.articleImages.map { image -> image.url }
            )
        }

        return PageArticlesResponse(
            articles = articleResponses.content,
            totalPages = articleResponses.totalPages
        )
    }

    @Transactional
    fun createArticle(articleCreateRequest: ArticleCreateRequest) {
        val user: User = userRepository.findByIdOrNull(articleCreateRequest.userId)
            ?: throw DefaultException(ErrorCode.USER_NOT_FOUND)

        val imageUrls = articleCreateRequest.images.map {
            s3Service.uploadImageToS3(
                userId = user.id,
                domain = DomainType.COMMUNITY.name,
                fileName = it.originalFilename!!,
                imageByteArray = it.bytes,
            )
        }

        val article = Article(
            topic = articleCreateRequest.topic,
            title = articleCreateRequest.title,
            content = articleCreateRequest.content,
            writer = user
        )

        val articleImages = imageUrls.map {
            ArticleImage(
                url = it,
                article = article
            )
        }

        articleRepository.save(article)

        runCatching {
            articleImageRepository.saveAll(articleImages)
        }.onFailure {
            imageUrls.map { url ->
                s3Service.deleteFromS3ByUrl(url)
            }
        }.getOrThrow()
    }
}