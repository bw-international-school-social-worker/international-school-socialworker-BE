package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.Class

interface ClassService {
    fun findById(id: Long): Class

    fun findAll(): MutableList<Class>

    fun save(classToSave: Class): Class

    fun update(classToUpdate: Class): Class

    fun delete(id: Long)

    fun assignToSchool(classId: Long, schoolId: Long)

    fun removeFromSchool(classId: Long, schoolId: Long)
}