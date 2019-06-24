package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import org.springframework.data.domain.Pageable

interface SchoolAdminService {
    fun findById(id: Long): SchoolAdmin

    fun findAll(pageable: Pageable): MutableList<SchoolAdmin>

    fun save(schoolAdmin: SchoolAdmin): SchoolAdmin

    fun update(schoolAdmin: SchoolAdmin, id: Long): SchoolAdmin

    fun delete(id: Long)
}