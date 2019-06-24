package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Grade
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface GradeRepository: PagingAndSortingRepository<Grade, Long> {
    @Modifying
    @Query("UPDATE grades SET school_id = :schoolId WHERE grade_id = :gradeId", nativeQuery = true)
    fun assignToSchool(gradeId: Long, schoolId: Long)

    @Modifying
    @Query("UPDATE grades SET school_id = null WHERE grade_id = :gradeId", nativeQuery = true)
    fun removeFromSchool(gradeId: Long)
}