package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.DEFAULT_ENCODING
import com.example.daitssuapi.common.enums.DomainType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.infra.service.S3Service
import com.example.daitssuapi.domain.main.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.main.dto.request.ArticleUpdateRequest
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.dto.response.PageArticlesResponse
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.ArticleImage
import com.example.daitssuapi.domain.main.model.entity.Comment
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.ArticleImageRepository
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class ArticleService(
    private val articleImageRepository: ArticleImageRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
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
    fun updateArticle(
            articleId: Long,
            articleUpdateRequest: ArticleUpdateRequest
            ){
        val user: User = userRepository.findByIdOrNull(articleUpdateRequest.userId)
                ?: throw DefaultException(ErrorCode.USER_NOT_FOUND)
        val article: Article = articleRepository.findByIdOrNull(articleId)
                ?: throw DefaultException(ErrorCode.ARTICLE_NOT_FOUND)


        article.title = articleUpdateRequest.title.toString()
        article.topic = articleUpdateRequest.topic
        article.content = articleUpdateRequest.content.toString()

        val newImageUrls = articleUpdateRequest.images?.mapNotNull { image ->
            // MultipartFile이 null이 아니고, 이미지 업로드가 성공한 경우에만 URL을 반환
            runCatching {
                s3Service.uploadImageToS3(
                        userId = user.id,
                        domain = DomainType.COMMUNITY.name,
                        fileName = image.originalFilename!!,
                        imageByteArray = image.bytes
                )
            }.getOrNull()
        }
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

    @Transactional
    fun writeComment(articleId: Long, request: CommentWriteRequest): CommentResponse {
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND)

        validateComment(article = article, content = request.content, originalCommentId = request.originalCommentId)

        val comment = commentRepository.save(Comment(
            writer = user,
            article = article,
            content = request.content,
            originalId = request.originalCommentId
        ))

        return CommentResponse(
            commentId = comment.id,
            userId = comment.writer.id,
            content = comment.content,
            originalCommentId = comment.originalId,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt
        )
    }

    private fun validateComment(article: Article, content: String, originalCommentId: Long?) {
        if (512 < content.toByteArray(Charset.forName(DEFAULT_ENCODING)).size) {
            throw DefaultException(errorCode = ErrorCode.COMMENT_TOO_LONG)
        }

        originalCommentId?.apply {
            val originalComment = commentRepository.findByIdOrNull(this)
                ?: throw DefaultException(errorCode = ErrorCode.COMMENT_NOT_FOUND)

            if (originalComment.article != article) {
                throw DefaultException(errorCode = ErrorCode.DIFFERENT_ARTICLE)
            }
        }
    }

    fun getComments(articleId: Long): List<CommentResponse> {
        articleRepository.findByIdOrNull(articleId)
            ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND)

        return commentRepository.findByArticleId(articleId = articleId).map {
            CommentResponse(
                commentId = it.id,
                userId = it.writer.id,
                content = it.content,
                originalCommentId = it.originalId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
}
