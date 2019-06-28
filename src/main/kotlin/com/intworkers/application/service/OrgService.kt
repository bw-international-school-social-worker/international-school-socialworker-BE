package com.intworkers.application.service

import com.intworkers.application.model.schoolsystem.Organization

interface OrgService {
    fun findById(id: Long): Organization

    fun findAll(): MutableList<Organization>

    fun save(org: Organization): Organization

    fun update(org: Organization, id: Long): Organization

    fun delete(id: Long)
}