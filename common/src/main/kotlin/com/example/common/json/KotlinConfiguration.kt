package com.example.common.json

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import com.fasterxml.jackson.module.kotlin.addSerializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.awt.Color
import kotlin.reflect.KClass

@Configuration
@ConditionalOnClass(name = ["kotlin.jvm.internal.Intrinsics"])
class KotlinConfiguration {
    @Primary
    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()
        .modulesToInstall(
            KotlinModule
                .Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, true)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build(),
            JavaTimeModule(),
            SimpleModule().apply {
                addSerializerAndDeserializer(
                    kClass = Color::class,
                    serializer = ColorSerializer(),
                    deserializer = ColorDeserializer(),
                )
            },
        )
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    private inline fun <reified T: Any> SimpleModule.addSerializerAndDeserializer(
        kClass: KClass<T>,
        serializer: JsonSerializer<T>,
        deserializer: JsonDeserializer<T>,
    ) {
        addSerializer(kClass, serializer)
        addDeserializer(kClass, deserializer)
    }
}
