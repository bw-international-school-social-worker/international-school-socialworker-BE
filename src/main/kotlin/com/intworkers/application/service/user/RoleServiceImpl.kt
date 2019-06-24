package com.intworkers.application.service.user

import com.intworkers.application.model.user.Role
import com.intworkers.application.repository.user.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityNotFoundException
import java.util.ArrayList

@Component
@Service(value = "roleService")
class RoleServiceImpl : RoleService {

    @Autowired
    lateinit var rolerepos: RoleRepository

    override fun findAll(): List<Role> {
        val list = ArrayList<Role>()
        rolerepos.findAll().iterator().forEachRemaining{ list.add(it) }
        return list
    }


    override fun findRoleById(id: Long): Role {
        return rolerepos.findById(id).orElseThrow { EntityNotFoundException(java.lang.Long.toString(id)) }
    }

    override fun findByName(name: String): Role {
        val rr = rolerepos.findByNameIgnoreCase(name)
        return rr ?: throw EntityNotFoundException(name)
    }

    override fun delete(id: Long) {
        rolerepos.findById(id).orElseThrow { EntityNotFoundException(java.lang.Long.toString(id)) }
        rolerepos.deleteById(id)
    }


    @Transactional
    override fun save(role: Role): Role {
        return rolerepos.save(role)
    }
}