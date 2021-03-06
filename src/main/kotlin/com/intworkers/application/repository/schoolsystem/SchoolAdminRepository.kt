package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.User
import org.springframework.data.repository.PagingAndSortingRepository

interface SchoolAdminRepository: PagingAndSortingRepository<SchoolAdmin, Long> {

}