package com.example.common.querydsl

import com.example.common.pagination.QueryDslOrder.Companion.toQueryDslOrderSpecifierArray
import com.example.common.pagination.QueryDslPageRequest
import com.querydsl.core.SimpleQuery
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

sealed class JpaQueryDslFactory(entityManager: EntityManager) : JPAQueryFactory(entityManager) {

    fun <T : Any> paginate(
        selectFrom: EntityPathBase<T>,
        pageRequest: QueryDslPageRequest<*>,
        vararg joinSpecifiers: JoinSpecifier = arrayOf(),
        query: JPAQuery<*>.() -> JPAQuery<*>,
    ): Page<T> {
        val entities = this
            .selectFrom(selectFrom)
            .apply {
                joinSpecifiers.forEach {
                    it.query(this)
                    if (it.shouldFetch) fetchJoin()
                }
            }
            .apply { query() }
            .paginate(pageRequest)
            .fetch()

        val count = this
            .select(selectFrom.count())
            .from(selectFrom)
            .apply { query() }
            .fetchFirst() ?: 0L

        return PageImpl(entities, pageRequest, count)
    }

    companion object {
        private fun <Q : SimpleQuery<Q>> SimpleQuery<Q>.paginate(queryDslPageRequest: QueryDslPageRequest<*>): Q {
            return this
                .offset(queryDslPageRequest.offset)
                .limit(queryDslPageRequest.pageSize.toLong())
                .orderBy(*queryDslPageRequest.orders.toQueryDslOrderSpecifierArray())
        }
    }
}

data class JoinSpecifier(
    val shouldFetch: Boolean = false,
    val query: JPAQuery<*>.() -> JPAQuery<*>,
)

class JpaQueryFactory(entityManager: EntityManager) : JpaQueryDslFactory(entityManager)
