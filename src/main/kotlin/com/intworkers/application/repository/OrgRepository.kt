package com.intworkers.application.repository

import com.intworkers.application.model.schoolsystem.Organization
import org.springframework.data.repository.PagingAndSortingRepository

interface OrgRepository: PagingAndSortingRepository<Organization, Long>