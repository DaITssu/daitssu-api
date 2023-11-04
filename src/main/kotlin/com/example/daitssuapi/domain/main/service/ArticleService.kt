package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.DEFAULT_ENCODING
import com.example.daitssuapi.common.enums.DomainType
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.infra.service.S3Service
import com.example.daitssuapi.domain.main.dto.request.ArticleCreateRequest
import com.example.daitssuapi.domain.main.dto.request.CommentWriteRequest
import com.example.daitssuapi.domain.main.dto.response.ArticleResponse
import com.example.daitssuapi.domain.main.dto.response.CommentResponse
import com.example.daitssuapi.domain.main.dto.response.PageArticlesResponse
import com.example.daitssuapi.domain.main.model.entity.*
import com.example.daitssuapi.domain.main.model.repository.*
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.time.LocalDate

@Service
class ArticleService(
    private val articleImageRepository: ArticleImageRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val scrapRepository: ScrapRepository,
    private val userRepository: UserRepository,
    private val s3Service: S3Service,
) {
    // TODO : 게시글 조회 시, 해당 유저의 스크랩 여부도 노출해야함
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
            imageUrls = article.images.map { it.url },
            likes = article.likes.size,
            comments = article.comments.size,
            scrapCount = article.scraps.size
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
                imageUrls = it.images.map { image -> image.url },
                likes = it.likes.size,
                comments = it.comments.size
            )
        }

        return PageArticlesResponse(
            articles = articleResponses.content,
            totalPages = articleResponses.totalPages
        )
    }

    fun getPopularArticles(): List<ArticleResponse> {
        val articles: List<Article> = articleRepository.findAllByCreatedAtIsLessThanEqual(
            createdAt = LocalDate.now().atStartOfDay()
        )

        articles.sortedBy { it.likes.size }

        return articles.map {
            ArticleResponse(
                id = it.id,
                topic = it.topic.value,
                title = it.title,
                content = it.content,
                writerNickName = it.writer.nickname!!,
                updatedAt = it.updatedAt,
                imageUrls = it.images.map { image -> image.url },
                likes = it.likes.size,
                comments = it.comments.size
            )
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
            writer = user,
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
    fun deleteArticle(articleId: Long) {
        val article: Article = articleRepository.findByIdOrNull(articleId)
            ?: throw DefaultException(ErrorCode.ARTICLE_NOT_FOUND)

        article.images.map {
            s3Service.deleteFromS3ByUrl(it.url)
        }

        articleRepository.delete(article)
    }

    @Transactional
    fun writeComment(articleId: Long, request: CommentWriteRequest): CommentResponse {
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND)

        validateComment(article = article, content = request.content, originalCommentId = request.originalCommentId)

        val comment = commentRepository.save(
            Comment(
                writer = user,
                article = article,
                content = request.content,
                originalId = request.originalCommentId
            )
        )

        return CommentResponse(
            commentId = comment.id,
            userId = comment.writer.id,
            content = comment.content,
            originalCommentId = comment.originalId,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt
        )
    }

    @Transactional
    fun like(
        articleId: Long,
        userId: Long,
    ) {
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND)

        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)

        val articleLike =
            if (articleLikeRepository.findByUserAndArticle(article = article, user = user) != null) {
                throw DefaultException(errorCode = ErrorCode.ALREADY_LIKED)
            } else {
                ArticleLike(
                    article = article,
                    user = user
                )
            }

        articleLikeRepository.save(articleLike)
    }

    @Transactional
    fun dislike(
        articleId: Long,
        userId: Long,
    ) {
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND)

        val user = userRepository.findByIdOrNull(userId)
            ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND)

        val articleLike = articleLikeRepository.findByUserAndArticle(article = article, user = user)
            ?: throw DefaultException(errorCode = ErrorCode.ALREADY_DISLIKED)

        articleLikeRepository.delete(articleLike)
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

    @Transactional
    fun scrapArticle(articleId: Long, userId: Long, isActive: Boolean) {
        val scrap = scrapRepository.findByArticleIdAndUserId(articleId = articleId, userId = userId)
            ?: Scrap(
                user = userRepository.findByIdOrNull(id = userId)
                    ?: throw DefaultException(errorCode = ErrorCode.USER_NOT_FOUND),
                article = articleRepository.findByIdOrNull(id = articleId)
                    ?: throw DefaultException(errorCode = ErrorCode.ARTICLE_NOT_FOUND),
                isActive = if (isActive) true else throw DefaultException(errorCode = ErrorCode.NEW_SCRAP_ISACTIVE_NOT_FALSE)
            )

        scrap.isActive = isActive
        scrapRepository.save(scrap)
    }
}
