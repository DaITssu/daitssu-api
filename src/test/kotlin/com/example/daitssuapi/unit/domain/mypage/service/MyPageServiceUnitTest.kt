package com.example.daitssuapi.unit.domain.mypage.service

import com.example.daitssuapi.domain.main.enums.Topic
import com.example.daitssuapi.domain.main.model.entity.Article
import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.domain.main.model.repository.ArticleRepository
import com.example.daitssuapi.domain.main.model.repository.CommentRepository
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
    private val mypageService: MypageService = spyk(
        objToCopy = MypageService(userRepository, articleRepository, commentRepository),
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
    
}