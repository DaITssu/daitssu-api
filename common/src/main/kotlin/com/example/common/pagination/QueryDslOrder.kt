package com.example.common.pagination

import com.querydsl.core.types.OrderSpecifier
import org.springframework.data.domain.Sort

interface QueryDslOrder {
    val order: OrderSpecifier<*>

    fun toJpaOrder(): Sort.Order = Sort.Order(
        if (order.isAscending) Sort.Direction.ASC else Sort.Direction.DESC,
        order.target.toString().split('.').last(),
    )

    companion object {
        fun <O: QueryDslOrder> List<O>.toJpaSort(): Sort =
            Sort.by(this.map { it.toJpaOrder() })

        fun <O: QueryDslOrder> List<O>.toQueryDslOrderSpecifierArray(): Array<OrderSpecifier<*>> = this
            .map { it.order }
            .toTypedArray()
    }
}
