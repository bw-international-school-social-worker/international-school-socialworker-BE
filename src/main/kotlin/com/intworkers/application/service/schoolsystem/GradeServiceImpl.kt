package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.model.schoolsystem.Grade
import com.intworkers.application.repository.schoolsystem.ClassRepository
import com.intworkers.application.repository.schoolsystem.GradeRepository
import com.intworkers.application.repository.schoolsystem.SchoolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Component
@Service(value = "gradeService")
class GradeServiceImpl : GradeService {

    @Autowired
    private lateinit var gradeRepository: GradeRepository

    @Autowired
    private lateinit var schoolRepository: SchoolRepository

    override fun findById(id: Long): Grade {
        return gradeRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find grade with id $id") }
    }

    override fun findAll(pageable: Pageable): MutableList<Grade> {
        val grades = mutableListOf<Grade>()
        gradeRepository.findAll(pageable).iterator().forEachRemaining { grades.add(it) }
        return grades
    }

    @Transactional
    @Modifying
    override fun save(grade: Grade): Grade {
        val newGrade = Grade()
        newGrade.gradeNumber = grade.gradeNumber
        newGrade.school = grade.school
        return gradeRepository.save(newGrade)
    }

    @Transactional
    @Modifying
    override fun update(grade: Grade, id: Long): Grade {
        val updatedGrade = gradeRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find grade with id $id ") }
        if (grade.gradeNumber != null) updatedGrade.gradeNumber = grade.gradeNumber
        if (grade.school != null) updatedGrade.school = grade.school
        return gradeRepository.save(updatedGrade)
    }

    @Transactional
    @Modifying
    override fun delete(id: Long) {
        if (gradeRepository.findById(id).isPresent)
            gradeRepository.deleteById(id)
        else throw ResourceNotFoundException("Couldn't find grade with id $id")
    }

    @Transactional
    override fun assignToSchool(gradeId: Long, schoolId: Long) {
        if (gradeRepository.findById(gradeId).isPresent
                && schoolRepository.findById(schoolId).isPresent)
            gradeRepository.assignToSchool(gradeId, schoolId)
        else throw ResourceNotFoundException("Couldn't find grade or School")
    }

    @Transactional
    override fun removeFromSchool(gradeId: Long) {
        if (gradeRepository.findById(gradeId).isPresent)
            gradeRepository.removeFromSchool(gradeId)
        else throw ResourceNotFoundException("Couldn't find grade or School")
    }
}