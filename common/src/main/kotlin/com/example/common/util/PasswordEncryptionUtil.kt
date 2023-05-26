package com.example.common.util

import java.security.MessageDigest

// 유저 검증 Util
object PasswordEncryptionUtil {
    fun encrypt(password: String): ByteArray {
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByteArray())
        return messageDigest.digest()
    }
}
