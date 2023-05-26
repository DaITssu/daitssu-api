package com.example.common.util

import com.example.common.exception.ColorFormatInvalidException
import java.awt.Color

object ColorUtil {
    @Suppress("MemberVisibilityCanBePrivate")
    fun String.parseColorOrNull(): Color? {
        if (!colorRegex.matches(this))
            return null

        val colorComponents = this
            .substring(1)
            .chunked(2)
            .map { it.toIntOrNull(16) ?: return null }

        return when (colorComponents.size) {
            3 -> Color(
                colorComponents[0],
                colorComponents[1],
                colorComponents[2],
            )

            4 -> Color(
                colorComponents[0],
                colorComponents[1],
                colorComponents[2],
                colorComponents[3],
            )

            else -> null
        }
    }

    fun String.parseColor(): Color = parseColorOrNull() ?: throw ColorFormatInvalidException()

    fun Color.toHexString(): String =
        String.format("#%02X%02X%02X%02X", red, green, blue, alpha)

    @Suppress("MemberVisibilityCanBePrivate")
    val colorRegex = "^#([A-F0-9]{6}|[A-F0-9]{8})\$".toRegex()
}
