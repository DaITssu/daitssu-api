package com.example.daitssuapi.security.type

import org.springframework.security.config.annotation.web.builders.HttpSecurity

typealias AuthorizeRequestsApplier = (HttpSecurity) -> (Unit)

