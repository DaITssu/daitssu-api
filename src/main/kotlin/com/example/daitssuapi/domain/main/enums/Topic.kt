package com.example.daitssuapi.domain.main.enums

enum class Topic(val value: String) {
    CHAT("잡담"),
    QUESTION("질문"),
    INFORMATION("정보");

    companion object {
        private val map = Topic.values().associateBy { it.value }
        operator fun get(value: String) = map[value]
    }
}