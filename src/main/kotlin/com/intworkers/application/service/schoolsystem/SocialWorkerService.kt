package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.user.SocialWorker
import org.springframework.data.domain.Pageable

interface SocialWorkerService {
    fun findById(id: Long): SocialWorker

    fun findAll(pageable: Pageable): MutableList<SocialWorker>

    fun save(socialWorker: SocialWorker): SocialWorker

    fun update(socialWorker: SocialWorker, id: Long): SocialWorker

    fun delete(id: Long)
}