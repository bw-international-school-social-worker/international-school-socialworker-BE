package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface SocialWorkerRepository: PagingAndSortingRepository<SocialWorker, Long> {
    @Modifying
    @Query("INSERT INTO worker_schools (worker_id, school_id) VALUES (:workerId, :schoolId)", nativeQuery = true)
    fun assignWorkerToSchool(workerId: Long, schoolId: Long)

    @Modifying
    @Query("DELETE FROM worker_schools WHERE worker_id = :workerId AND school_id = :schoolId", nativeQuery = true)
    fun removeWorkerFromSchool(workerId: Long, schoolId: Long)
}