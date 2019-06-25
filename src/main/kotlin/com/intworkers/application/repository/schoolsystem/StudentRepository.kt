package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Student
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface StudentRepository: CrudRepository<Student, Long> {
    /* @Modifying
    @Query("UPDATE students SET worker_id = :workerId WHERE student_id = :studentId ", nativeQuery = true)
    fun assignToWorker(studentId: Long, workerId: Long)

    @Modifying
    @Query("UPDATE students SET class_id = :classId WHERE student_id = :studentId ", nativeQuery = true)
    fun assignToClass(studentId: Long, classId: Long)

    @Modifying
    @Query("UPDATE students SET grade_id = :gradeId WHERE student_id = :studentId ", nativeQuery = true)
    fun assignToGrade(studentId: Long, gradeId: Long)

    @Modifying
    @Query("UPDATE students SET school_id = :schoolId WHERE student_id = :studentId ", nativeQuery = true)
    fun assignToSchool(studentId: Long, schoolId: Long) */
}