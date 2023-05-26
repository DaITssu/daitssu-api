package com.example.common.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.example.common.util.ColorUtil.parseColor
import org.springframework.boot.jackson.JsonComponent
import java.awt.Color

@JsonComponent
class ColorDeserializer: JsonDeserializer<Color>() {
    @Throws
    override fun deserialize(
        jsonParser: JsonParser,
        deserializationContext: DeserializationContext
    ): Color = jsonParser.text.parseColor()
}
