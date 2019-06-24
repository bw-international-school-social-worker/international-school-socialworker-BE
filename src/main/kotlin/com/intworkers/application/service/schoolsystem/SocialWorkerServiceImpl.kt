package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.repository.schoolsystem.SocialWorkerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service(value = "socialWorkerService")
class SocialWorkerServiceImpl : SocialWorkerService {

    @Autowired
    private lateinit var socialWorkerRepository: SocialWorkerRepository

    override fun findById(id: Long): SocialWorker {
        return socialWorkerRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find social worker with id $id") }
    }

    override fun findAll(pageable: Pageable): MutableList<SocialWorker> {
        val workers = mutableListOf<SocialWorker>()
        socialWorkerRepository.findAll().iterator().forEachRemaining { workers.add(it) }
        return workers
    }

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

    override fun update(socialWorker: SocialWorker, id: Long): SocialWorker {
        val updatedWorker = socialWorkerRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find social worker with id $id") }
        if (socialWorker.email != null) updatedWorker.email = socialWorker.email
        if (socialWorker.firstName != null) updatedWorker.firstName = socialWorker.firstName
        if (socialWorker.lastName != null) updatedWorker.lastName = socialWorker.lastName
        if (socialWorker.phone != null) updatedWorker.phone = socialWorker.phone
        if (socialWorker.photoUrl != null) updatedWorker.photoUrl = socialWorker.photoUrl
        return socialWorkerRepository.save(updatedWorker)
    }

    override fun delete(id: Long) {
        if (socialWorkerRepository.findById(id).isPresent) {
            socialWorkerRepository.deleteById(id)
        } else throw ResourceNotFoundException("Couldn't find social worker with id $id")
    }
}