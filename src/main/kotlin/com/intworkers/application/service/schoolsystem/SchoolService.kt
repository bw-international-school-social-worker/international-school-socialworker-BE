package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.School
import org.springframework.data.domain.Pageable

interface SchoolService {
    fun findById(id: Long): School

    fun findAll(pageable: Pageable): MutableList<School>

    fun save(school: School): School

    fun update(school: School, id: Long): School

    fun delete(id: Long)
}