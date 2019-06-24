package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.Student
import org.springframework.data.domain.Pageable

interface StudentService {
    fun findById(id: Long): Student

    fun findAll(pageable: Pageable): MutableList<Student>

    fun save(student: Student): Student

    fun update(student: Student, id: Long): Student

    fun assignToWorker(studentId: Long, workerId: Long)

    fun assignToClass(studentId: Long, classId: Long)

    fun assignToGrade(studentId: Long, gradeId: Long)

    fun assignToSchool(studentId: Long, schoolId: Long)

    fun delete(studentId: Long)
}