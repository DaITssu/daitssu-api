package com.example.common.querydsl.configuration

import com.example.common.querydsl.JpaQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfiguration(
    private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryDslFactory() = JpaQueryFactory(entityManager)
}
