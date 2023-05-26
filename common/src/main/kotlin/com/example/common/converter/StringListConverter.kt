package com.example.common.converter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

class StringListConverter : AttributeConverter<List<String>, String> {
    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<String>): String =
        objectMapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): List<String> =
        objectMapper.readValue(dbData, Array<String>::class.java).toList()
}

