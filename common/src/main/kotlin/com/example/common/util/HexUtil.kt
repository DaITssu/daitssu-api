package com.example.common.util

object HexUtil {
    fun String.parseHex(): ByteArray = chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()

    fun ByteArray.toHexString(): String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}
