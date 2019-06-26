package com.intworkers.application.service.schoolsystem

import com.intworkers.application.model.schoolsystem.Visit


interface VisitService {
    fun findById(id: Long): Visit

    fun findAll(): MutableList<Visit>

    fun findAllWorkerVisits(workerId: Long): MutableList<Visit>

    fun findAllSchoolVisits(schoolId: Long): MutableList<Visit>

    fun save(visit: Visit): Visit

    fun update(visit: Visit, visitId: Long): Visit

    fun delete(visitId: Long)
}
