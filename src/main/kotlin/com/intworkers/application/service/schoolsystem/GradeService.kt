package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.model.schoolsystem.Grade
import org.springframework.data.domain.Pageable

interface GradeService {
    fun findById(id: Long): Grade

    fun findAll(pageable: Pageable): MutableList<Grade>

    fun save(grade: Grade): Grade

    fun update(grade: Grade, id: Long): Grade

    fun delete(id: Long)

    fun assignToSchool(gradeId: Long, schoolId: Long)

    fun removeFromSchool(gradeId: Long)
}