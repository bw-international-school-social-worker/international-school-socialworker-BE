package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Visit
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface VisitRepository: PagingAndSortingRepository<Visit, Long>