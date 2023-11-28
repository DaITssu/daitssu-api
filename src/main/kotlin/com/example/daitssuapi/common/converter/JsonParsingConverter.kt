package com.example.daitssuapi.common.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonParsingConverter: AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>): String =
            attribute.let { jacksonObjectMapper().writeValueAsString(mapOf("url" to it)) }

    override fun convertToEntityAttribute(dbData: String): List<String> {
        val urlNode = jacksonObjectMapper().readTree(dbData).findValues("url").firstOrNull()

        return if (urlNode != null && urlNode.isArray && urlNode.size() > 0) {
            urlNode.map { it.textValue() }
        } else {
            emptyList()
        }
    }
}
