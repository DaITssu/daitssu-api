package com.example.common.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReflectionConfiguration {
    @Bean(BeanName.BASE_PACKAGE)
    fun basePackage(): String = BASE_PACKAGE

    object BeanName {
        const val BASE_PACKAGE = "basePackage"
    }

    companion object {
        const val BASE_PACKAGE = "com.example"
    }
}
