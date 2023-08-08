package com.example.daitssuapi.domain.main.enums

enum class Topic(name: String) {
    CHAT("잡담"),
    QUESTION("질문"),
    INFORMATION("정보");

    companion object {
        private val map = Topic.values().associateBy { it.name }
        operator fun get(name: String) = map[name]
    }
}