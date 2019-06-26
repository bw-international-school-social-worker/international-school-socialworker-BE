package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.School
import com.intworkers.application.repository.schoolsystem.SchoolAdminRepository
import com.intworkers.application.repository.schoolsystem.SchoolRepository
import com.intworkers.application.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service(value = "schoolService")
class SchoolServiceImpl: SchoolService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var schoolAdminRepository: SchoolAdminRepository

    @Autowired
    private lateinit var schoolRepository: SchoolRepository

    override fun findById(id: Long): School {
        return schoolRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find school with id $id") }
    }

    override fun findAll(pageable: Pageable): MutableList<School> {
        val schools = mutableListOf<School>()
        schoolRepository.findAll(pageable).iterator().forEachRemaining { schools.add(it) }
        return schools
    }

    override fun save(school: School): School {
        val newSchool = School()
        newSchool.schoolName = school.schoolName
        newSchool.dateEstablished = school.dateEstablished
        newSchool.schoolAdmin = school.schoolAdmin
        return schoolRepository.save(newSchool)
    }

    override fun update(school: School, id: Long): School {
        val updatedSchool = schoolRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find school with id $id") }
        if (school.schoolName != null) updatedSchool.schoolName = school.schoolName
        if (school.dateEstablished != null) updatedSchool.dateEstablished = school.dateEstablished
        if (school.schoolAdmin != null) updatedSchool.schoolAdmin = school.schoolAdmin
        return schoolRepository.save(updatedSchool)
    }

    override fun delete(id: Long) {
        if (schoolRepository.findById(id).isPresent) {
            schoolRepository.deleteById(id)
        } else {
            throw ResourceNotFoundException("Couldn't find school with id $id")
        }
    }
}