package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Course
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface ClassRepository: PagingAndSortingRepository<Course, Long> {
    @Modifying
    @Query("UPDATE classes SET school_id = :schoolId WHERE class_id = :classId", nativeQuery = true)
    fun assignToSchool(classId: Long, schoolId: Long)

    @Modifying
    @Query("UPDATE classes SET school_id = null WHERE class_id = :classId", nativeQuery = true)
    fun removeFromSchool(classId: Long)

}