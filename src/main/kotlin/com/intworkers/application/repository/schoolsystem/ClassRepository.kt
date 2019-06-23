package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Class
import org.springframework.data.repository.PagingAndSortingRepository

interface ClassRepository: PagingAndSortingRepository<Class, Long>