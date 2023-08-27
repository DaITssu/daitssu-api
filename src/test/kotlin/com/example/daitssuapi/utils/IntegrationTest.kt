package com.example.daitssuapi.utils

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@UnitTest
@Sql(value = ["classpath:/h2-data.sql"])
annotation class IntegrationTest
