
package com.example.daitssuapi.domain.main.model.repository

import com.example.daitssuapi.domain.main.model.entity.ServiceNotice
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceNoticeRepository : JpaRepository<ServiceNotice, Long> {

}