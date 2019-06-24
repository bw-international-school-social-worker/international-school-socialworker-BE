package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.repository.schoolsystem.SchoolAdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service(value = "schoolAdminService")
class SchoolAdminServiceImpl: SchoolAdminService {
    
    @Autowired
    private lateinit var schoolAdminRepository: SchoolAdminRepository
    
    override fun findById(id: Long): SchoolAdmin {
        return schoolAdminRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find school admin with id $id") }
    }

    override fun findAll(pageable: Pageable): MutableList<SchoolAdmin> {
        val admins = mutableListOf<SchoolAdmin>()
        schoolAdminRepository.findAll().iterator().forEachRemaining { admins.add(it) }
        return admins
    }

    override fun save(schoolAdmin: SchoolAdmin): SchoolAdmin {
        val newAdmin = SchoolAdmin()
        if (schoolAdmin.user != null) {
            newAdmin.user = schoolAdmin.user
            newAdmin.email = schoolAdmin.email
            newAdmin.firstName = schoolAdmin.firstName
            newAdmin.lastName = schoolAdmin.lastName
            newAdmin.phone = schoolAdmin.phone
            newAdmin.photoUrl = schoolAdmin.photoUrl
        } else throw ResourceNotFoundException("Not a valid school admin")
        return schoolAdminRepository.save(newAdmin)
    }

    override fun update(schoolAdmin: SchoolAdmin, id: Long): SchoolAdmin {
        val updatedAdmin = schoolAdminRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find school admin with id $id") }
        if (schoolAdmin.email != null) updatedAdmin.email = schoolAdmin.email
        if (schoolAdmin.firstName != null) updatedAdmin.firstName = schoolAdmin.firstName
        if (schoolAdmin.lastName != null) updatedAdmin.lastName = schoolAdmin.lastName
        if (schoolAdmin.phone != null) updatedAdmin.phone = schoolAdmin.phone
        if (schoolAdmin.photoUrl != null) updatedAdmin.photoUrl = schoolAdmin.photoUrl
        return schoolAdminRepository.save(updatedAdmin)
    }

    override fun delete(id: Long) {
        if (schoolAdminRepository.findById(id).isPresent) {
            schoolAdminRepository.deleteById(id)
        } else throw ResourceNotFoundException("Couldn't find school admin with id $id")
    }

}