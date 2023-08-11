package com.example.daitssuapi.util

import java.security.MessageDigest

object PasswordEncryptionUtil {
    fun encrypt(password: String): ByteArray {
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByteArray())
        return messageDigest.digest()
    }

    infix fun String.isEqualToEncryptedByteArray(passwordByteArray: ByteArray?): Boolean =
        encrypt(password = this).contentEquals(passwordByteArray)

    infix fun String.isNotEqualToEncryptedByteArray(passwordByteArray: ByteArray?): Boolean =
        !isEqualToEncryptedByteArray(passwordByteArray = passwordByteArray)

    fun String.parseHex(): ByteArray = chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()

    fun ByteArray.toHexString(): String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}
