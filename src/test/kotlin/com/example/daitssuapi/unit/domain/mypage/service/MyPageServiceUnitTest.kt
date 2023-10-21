package com.example.daitssuapi.unit.domain.mypage.service

import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.entity.Scrap
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
import com.example.daitssuapi.domain.main.model.repository.ScrapRepository
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.domain.mypage.service.MypageService
import com.example.daitssuapi.utils.UnitTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.repository.findByIdOrNull

@UnitTest
class MyPageServiceUnitTest {
    private val articleRepository: ArticleRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val commentRepository: CommentRepository = mockk(relaxed = true)
    private val scrapRepository: ScrapRepository = mockk(relaxed = true)
    private val mypageService: MypageService = spyk(
        objToCopy = MypageService(userRepository, articleRepository, commentRepository,scrapRepository),
        recordPrivateCalls = true
    )
    
    @Test
    @DisplayName("게시글이 있는 userId를 이용하여 게시글 조회시 _ 1개 이상의 게시글이 조회된다")
    fun get_my_articles_with_user_id(){
        val department = Department(name = "소프트웨어")
        val user = User(studentId = 20330333, name = "김철수", department = department, term = 4)
        val articles = listOf(Article(
            topic = Topic.CHAT,
            title = "하이",
            content = "하이하이",
            writer = user
        ))
        every { userRepository.findByIdOrNull(user.studentId.toLong()) } returns user
        every { articleRepository.findAllByWriter(user) } returns articles
        
        val findArticles = mypageService.getMyArticle(userId = user.studentId.toLong())
        
        assertAll(
            { assertThat(findArticles.size).isEqualTo(1) },
            { assertThat(findArticles.get(0).topic).isEqualTo("잡담") },
            { assertThat(findArticles.get(0).title).isEqualTo("하이") },
            { assertThat(findArticles.get(0).content).isEqualTo("하이하이") },
            { assertThat(findArticles.get(0).commentSize).isEqualTo(0) }
        )
        
        
    }
    
    @Test
    @DisplayName("게시글이 없는 userId를 이용하여 게시글 조회시 _ 빈 리스트가 조회된다")
    fun get_my_articles_with_wrong_user_id(){
        val department = Department(name = "소프트웨어")
        val user = User(studentId = 1, name = "김철수", department = department, term = 4)
        every { userRepository.findByIdOrNull(user.studentId.toLong()) } returns user
        every { articleRepository.findAllByWriter(user) } returns emptyList()
        
        val findArticles = mypageService.getMyArticle(userId = user.studentId.toLong())
        
        assertAll(
            { assertThat(findArticles).isEmpty() }
        )
        
    }
    
    @Test
    @DisplayName("스크랩한 게시글이 있는 userId를 이용하여 스크랩 조회시 _ 1개 이상의 게시글이 조회된다")
    fun get_my_scraps_with_user_id() {
        val department = Department(name = "소프트웨어")
        val user = User(studentId = 20330333, name = "김철수", department = department, term = 4)
        val article = Article(
            topic = Topic.CHAT,
            title = "하이",
            content = "하이하이",
            writer = user
        )
        val scrap = listOf(Scrap(user = user, article = article, isActive = true))
        
        every { userRepository.findByIdOrNull(user.id) } returns user
        every { scrapRepository.findByUserAndIsActiveTrue(user) } returns scrap
        
        val findScrap = mypageService.getMyScrap(userId = user.id)
        
        assertAll(
            { assertThat(findScrap.size).isEqualTo(1) },
            { assertThat(findScrap.get(0).id).isEqualTo(article.id) },
            { assertThat(findScrap.get(0).topic).isEqualTo("잡담") },
            { assertThat(findScrap.get(0).title).isEqualTo(article.title) },
            { assertThat(findScrap.get(0).content).isEqualTo(article.content) },
        )
        
    }
    
    @Test
    @DisplayName("스크랩한 게시글이 없는 userId를 받으면 조회시_ 빈 List가 출력된다")
    fun get_my_scraps_with_wrong_user_id() {
        val department = Department(name = "소프트웨어")
        val user = User(studentId = 20330333, name = "김철수", department = department, term = 4)
        every { userRepository.findByIdOrNull(user.id) } returns user
        every { scrapRepository.findByUserAndIsActiveTrue(user) } returns emptyList()
        
        val findScrap = mypageService.getMyScrap(userId = user.id)
        
        assertAll(
            { assertThat(findScrap).isEmpty()}
        )
    }
    
}