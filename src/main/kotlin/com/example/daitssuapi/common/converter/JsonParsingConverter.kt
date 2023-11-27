package com.example.daitssuapi.common.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonParsingConverter: AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>): String =
        """{"url" : [${attribute.joinToString(", ")}]}"""

    override fun convertToEntityAttribute(dbData: String): List<String> =
        jacksonObjectMapper().readTree(dbData).findValuesAsText("url")
}
