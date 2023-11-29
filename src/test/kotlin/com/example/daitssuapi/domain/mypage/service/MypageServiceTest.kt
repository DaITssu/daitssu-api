package com.example.daitssuapi.domain.mypage.service

import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows


@IntegrationTest
class MypageServiceTest (
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val mypageService:MypageService
) {
    
    @Test
    @DisplayName("올바른 userId를 받으면 작성한 게시글이 조회된다")
    fun get_my_articles_with_user_id() {
        val userId =4L
        val user = userRepository.findById(userId).get()
        val articles = articleRepository.findAllByWriterOrderByCreatedAtDesc(user)
        val findArticles = mypageService.getMyArticle(userId = userId)
        
        assertAll(
            { assertThat(findArticles).isNotEmpty },
            { assertThat(findArticles.size).isEqualTo(articles.size) }
        )
    }
    
    @Test
    @DisplayName("없는 userId를 받으면 USER_NOT_FOUND 에러가 발생한다")
    fun get_my_articles_with_wrong_user_id(){
        val wrongUserId = 999L
        
        assertThrows<DefaultException> {
            mypageService.getMyArticle(userId = wrongUserId)
        }
    }
}