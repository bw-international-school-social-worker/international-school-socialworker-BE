package com.intworkers.application.service

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Organization
import com.intworkers.application.repository.OrgRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service(value = "orgService")
class OrgServiceImpl: OrgService {

    @Autowired
    private lateinit var orgRepository: OrgRepository

    override fun findById(id: Long): Organization {
        return orgRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find Organization with id $id") }
    }

    override fun findAll(): MutableList<Organization> {
        val orgList = mutableListOf<Organization>()
        orgRepository.findAll().iterator().forEachRemaining { orgList.add(it) }
        return orgList
    }

    override fun save(org: Organization): Organization {
        val newOrg = Organization()
        newOrg.organizationName = org.organizationName
        newOrg.organizationMission = org.organizationMission
        return orgRepository.save(newOrg)
    }

    override fun update(org: Organization, id: Long): Organization {
        val updateOrg = orgRepository.findById(id)
                .orElseThrow {  ResourceNotFoundException("Couldn't find Organization with id $id") }
        if (org.organizationName != null) updateOrg.organizationName = org.organizationName
        if (org.organizationMission != null) updateOrg.organizationMission = org.organizationMission
        return orgRepository.save(updateOrg)
    }

    override fun delete(id: Long) {
        orgRepository.deleteById(id)
    }
}