package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Visit
import com.intworkers.application.repository.schoolsystem.VisitRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service(value = "visitService")
class VisitServiceImpl: VisitService {

    @Autowired
    private lateinit var visitRepository: VisitRepository

    override fun findById(id: Long): Visit {
        return visitRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find Visit with id $id") }
    }

    override fun findAll(): MutableList<Visit> {
        val visits = mutableListOf<Visit>()
        visitRepository.findAll().iterator().forEachRemaining { visits.add(it) }
        return visits
    }

    override fun findAllWorkerVisits(workerId: Long): MutableList<Visit> {
        val visits = mutableListOf<Visit>()
        val workerVisits = mutableListOf<Visit>()
        visitRepository.findAll().iterator().forEachRemaining { visits.add(it) }
        for (visit in visits) {
            if (visit.workerId == workerId) workerVisits.add(visit)
        }
        return workerVisits
    }

    override fun findAllSchoolVisits(schoolId: Long): MutableList<Visit> {
        val visits = mutableListOf<Visit>()
        val workerVisits = mutableListOf<Visit>()
        visitRepository.findAll().iterator().forEachRemaining { visits.add(it) }
        for (visit in visits) {
            if (visit.schoolId == schoolId) workerVisits.add(visit)
        }
        return workerVisits
    }

    override fun save(visit: Visit): Visit {
        var newVisit = Visit()
        newVisit.visitReason = visit.visitReason
        newVisit.visitDescription = visit.visitDescription
        newVisit.visitDate = visit.visitDate
        newVisit.school = visit.school
        newVisit.worker = visit.worker
        newVisit = visitRepository.save(newVisit)
        return visitRepository.findById(newVisit.visitId).orElseThrow()
    }

    override fun update(visit: Visit, visitId: Long): Visit {
        var updatedVisit = Visit()
        if (visit.visitReason != null) updatedVisit.visitReason = visit.visitReason
        if (visit.visitDescription != null) updatedVisit.visitDescription = visit.visitDescription
        if (visit.visitDate != null) updatedVisit.visitDate = visit.visitDate
        updatedVisit = visitRepository.save(updatedVisit)
        return visitRepository.findById(updatedVisit.visitId).orElseThrow()
    }

    override fun delete(visitId: Long) {
        if (visitRepository.findById(visitId).isPresent) visitRepository.deleteById(visitId)
    }
}