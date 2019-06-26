package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.Student
import org.springframework.data.domain.Pageable

interface StudentService {
    fun findById(id: Long): Student

    fun findAll(): MutableList<Student>

    fun save(student: Student): Student

    fun update(student: Student, id: Long): Student

    fun assignToWorker(studentId: Long, student: Student)

    fun assignToClass(studentId: Long, student: Student)

    fun assignToGrade(studentId: Long, student: Student)

    fun assignToSchool(studentId: Long, student: Student)

    fun removeFromWorker(studentId: Long, student: Student)

    fun removeFromClass(studentId: Long, student: Student)

    fun removeFromGrade(studentId: Long, student: Student)

    fun removeFromSchool(studentId: Long, student: Student)

    fun delete(studentId: Long)
}