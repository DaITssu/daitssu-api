package com.example.daitssuapi.domain.mypage.service

import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.ScrapRepository
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
    private val scrapRepository: ScrapRepository,
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

    @Test
    @DisplayName("올바른 userId를 받으면 스크랩한 게시글이 조회된다")
    fun get_my_scraps_with_user_id() {
        val userId = 1L
        val user = userRepository.findById(userId).get()
        val scraps = scrapRepository.findByUserAndIsActiveTrueOrderByCreatedAtDesc(user)
        val findScraps = mypageService.getMyScrap(userId)
        
        System.out.println(findScraps)
        
        assertAll(
            { assertThat(findScraps).isNotEmpty },
            { assertThat(findScraps.size).isEqualTo(scraps.size) },
        )
    }
    
    @Test
    @DisplayName("스크랩이 false이면 스크랩한 게시글이 조회가 안된다")
    fun get_my_scraps_with_is_active_false() {
        val userId = 3L
        val user = userRepository.findById(userId).get()
        val scrap = scrapRepository.findByUserAndIsActiveTrueOrderByCreatedAtDesc(user)
        val findScrapRepository = mypageService.getMyScrap(userId)
        
        assertAll(
            { assertThat(scrap).isEmpty()},
            { assertThat(findScrapRepository).isEmpty()}
        )
    }
    
    @Test
    @DisplayName("없는 userId를 받으면 USER_NOT_FOUND 에러가 발생한다")
    fun get_my_scraps_with_wrong_user_id() {
        val wrongUserId = 999L
        
        assertThrows<DefaultException> {
            mypageService.getMyScrap(userId = wrongUserId)
        }
    }
}