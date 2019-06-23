package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.user.User
import org.springframework.data.repository.PagingAndSortingRepository

interface ServiceWorkerRepository: PagingAndSortingRepository<User, Long>