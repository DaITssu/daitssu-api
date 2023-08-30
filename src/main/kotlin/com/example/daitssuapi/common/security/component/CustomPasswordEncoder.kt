package com.example.daitssuapi.common.security.component

import org.springframework.security.crypto.password.PasswordEncoder
import java.security.MessageDigest

class CustomPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String {
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(rawPassword.toString().toByteArray())
        return messageDigest.digest().toString()
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return encode(rawPassword = rawPassword) == encodedPassword
    }
}