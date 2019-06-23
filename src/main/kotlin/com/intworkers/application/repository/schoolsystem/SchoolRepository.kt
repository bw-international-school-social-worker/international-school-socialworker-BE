package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.School
import org.springframework.data.repository.PagingAndSortingRepository

interface SchoolRepository: PagingAndSortingRepository<School, Long>