package com.example.domain.main.enums

import com.example.common.enums.ErrorCode
import com.example.common.exception.DefaultException
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class Topic(val value: String) {
    CHAT("잡담"),
    QUESTION("질문"),
    INFORMATION("정보");
}