package com.example.common.querydsl

import com.example.common.pagination.QueryDslPageRequest
import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.jpa.impl.JPADeleteClause
import com.querydsl.jpa.impl.JPAInsertClause
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAUpdateClause
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page

sealed interface QueryDslRepository {
    val jpaQueryFactory: JpaQueryFactory

    fun <T : Any> paginate(
        selectFrom: EntityPathBase<T>,
        pageRequest: QueryDslPageRequest<*>,
        vararg joinSpecifiers: JoinSpecifier,
        query: JPAQuery<*>.() -> JPAQuery<*>,
    ): Page<T> = jpaQueryFactory.paginate(
        selectFrom = selectFrom,
        pageRequest = pageRequest,
        joinSpecifiers = joinSpecifiers,
        query = query,
    )

    fun <T> select(expr: Expression<T>): JPAQuery<T> = jpaQueryFactory.select(expr)
    fun <T> selectDistinct(expr: Expression<T>): JPAQuery<T> = jpaQueryFactory.selectDistinct(expr)
    fun <T> selectFrom(from: EntityPath<T>): JPAQuery<T> = jpaQueryFactory.selectFrom(from)
    fun <T> selectDistinctFrom(from: EntityPath<T>): JPAQuery<T> = jpaQueryFactory.selectFrom(from).distinct()

    fun selectOne(): JPAQuery<Int> = jpaQueryFactory.selectOne()
    fun selectZero(): JPAQuery<Int> = jpaQueryFactory.selectZero()

    fun insert(path: EntityPath<*>): JPAInsertClause = jpaQueryFactory.insert(path)
    fun update(path: EntityPath<*>): JPAUpdateClause = jpaQueryFactory.update(path)
    fun delete(from: EntityPath<*>): JPADeleteClause = jpaQueryFactory.delete(from)

    fun `if`(predicate: Boolean, then: () -> Predicate): Predicate? =
        if (predicate) then() else null
}

abstract class QueryRepository : QueryDslRepository {
    @Autowired
    override lateinit var jpaQueryFactory: JpaQueryFactory
}




