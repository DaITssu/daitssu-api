package com.example.daitssuapi.common.security.component

import com.example.daitssuapi.common.util.PasswordEncryptionUtil.encrypt
import com.example.daitssuapi.common.util.PasswordEncryptionUtil.toHexString
import org.springframework.security.crypto.password.PasswordEncoder

class CustomPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String {
        return encrypt(rawPassword.toString()).toHexString()
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return encode(rawPassword) == encodedPassword || rawPassword == encodedPassword
    }
}
