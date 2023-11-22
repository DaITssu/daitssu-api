package com.example.daitssuapi.utils

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
annotation class UnitTest

@SpringBootTest
@Transactional
@UnitTest
@Sql(value = ["classpath:/h2-data.sql"])
annotation class IntegrationTest

@IntegrationTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
annotation class ControllerTest
