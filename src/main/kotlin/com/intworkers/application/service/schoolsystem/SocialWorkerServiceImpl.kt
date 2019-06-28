package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.repository.schoolsystem.SchoolRepository
import com.intworkers.application.repository.schoolsystem.SocialWorkerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Component
@Service(value = "socialWorkerService")
class SocialWorkerServiceImpl : SocialWorkerService {

    @Autowired
    private lateinit var socialWorkerRepository: SocialWorkerRepository

    @Autowired
    private lateinit var schoolRepository: SchoolRepository

    override fun findById(id: Long): SocialWorker {
        return socialWorkerRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find social worker with id $id") }
    }

    override fun findAll(pageable: Pageable): MutableList<SocialWorker> {
        val workers = mutableListOf<SocialWorker>()
        socialWorkerRepository.findAll().iterator().forEachRemaining { workers.add(it) }
        return workers
    }

    @Transactional
    @Modifying
    override fun save(socialWorker: SocialWorker): SocialWorker {
        val newWorker = SocialWorker()
        if (socialWorker.user != null) {
            newWorker.workerid = socialWorker.workerid
            newWorker.user = socialWorker.user
            newWorker.email = socialWorker.email
            newWorker.firstName = socialWorker.firstName
            newWorker.lastName = socialWorker.lastName
            newWorker.phone = socialWorker.phone
            newWorker.photoUrl = socialWorker.photoUrl
        } else throw ResourceNotFoundException("Not a valid worker")
        return socialWorkerRepository.save(newWorker)
    }

    @Transactional
    @Modifying
    override fun update(socialWorker: SocialWorker, id: Long): SocialWorker {
        val updatedWorker = socialWorkerRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find social worker with id $id") }
        if (socialWorker.email != null) updatedWorker.email = socialWorker.email
        if (socialWorker.firstName != null) updatedWorker.firstName = socialWorker.firstName
        if (socialWorker.lastName != null) updatedWorker.lastName = socialWorker.lastName
        if (socialWorker.phone != null) updatedWorker.phone = socialWorker.phone
        if (socialWorker.photoUrl != null) updatedWorker.photoUrl = socialWorker.photoUrl
        if (socialWorker.organization != null) updatedWorker.organization = socialWorker.organization
        return socialWorkerRepository.save(updatedWorker)
    }

    @Transactional
    @Modifying
    override fun delete(id: Long) {
        if (socialWorkerRepository.findById(id).isPresent) {
            socialWorkerRepository.deleteById(id)
        } else throw ResourceNotFoundException("Couldn't find social worker with id $id")
    }

    @Transactional
    override fun assignWorkerToSchool(workerId: Long, schoolId: Long) {
        if (socialWorkerRepository.findById(workerId).isPresent &&
                schoolRepository.findById(schoolId).isPresent) {
            socialWorkerRepository.assignWorkerToSchool(workerId, schoolId)
        }
    }

    @Transactional
    override fun removeWorkerFromSchool(workerId: Long, schoolId: Long) {
        socialWorkerRepository.removeWorkerFromSchool(workerId, schoolId)
    }

    override fun leaveOrg(id: Long) {
        val worker = socialWorkerRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find worker with id $id") }
        worker.organization = null
        socialWorkerRepository.save(worker)
    }
}