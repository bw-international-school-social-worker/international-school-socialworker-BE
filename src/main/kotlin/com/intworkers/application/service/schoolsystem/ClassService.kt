package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.Course
import org.springframework.data.domain.Pageable

interface ClassService {
    fun findById(id: Long): Course

    fun findAll(pageable: Pageable): MutableList<Course>

    fun save(classToSave: Course): Course

    fun update(classToUpdate: Course, id: Long): Course

    fun delete(id: Long)

    fun assignToSchool(classId: Long, schoolId: Long)

    fun removeFromSchool(classId: Long)
}