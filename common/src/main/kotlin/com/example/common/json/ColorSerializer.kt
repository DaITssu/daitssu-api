package com.example.common.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.example.common.util.ColorUtil.toHexString
import org.springframework.boot.jackson.JsonComponent
import java.awt.Color

@JsonComponent
class ColorSerializer: JsonSerializer<Color>() {
    override fun serialize(
        color: Color,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeString(color.toHexString())
    }
}
