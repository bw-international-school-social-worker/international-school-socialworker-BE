package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Grade
import org.springframework.data.repository.PagingAndSortingRepository

interface GradeRepository: PagingAndSortingRepository<Grade, Long>