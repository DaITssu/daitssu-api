package com.example.common.pagination

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.Min
import com.example.common.pagination.QueryDslOrder.Companion.toJpaSort
import org.springframework.data.domain.PageRequest

@JsonIgnoreProperties(
    "sort",
    "offset",
    "pageNumber",
    "pageSize",
    "paged",
    "unpaged",
)
abstract class QueryDslPageRequest<O: QueryDslOrder>(
    @Min(1)
    open val size: Int,
    @Min(0)
    open val page: Int, // zero-based
    open val orders: List<O>,
): PageRequest(page, size, orders.toJpaSort())




