package com.intworkers.application.service

import com.intworkers.application.model.SchoolAdmin
import com.intworkers.application.model.UserRoles
import com.intworkers.application.repository.RoleRepository
import com.intworkers.application.repository.SchoolAdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityNotFoundException
import java.util.ArrayList

@Component
@Service(value = "userService")
class UserServiceImpl : UserDetailsService, UserService {

    @Autowired
    lateinit var schoolAdminRepo: SchoolAdminRepository

    @Autowired
    lateinit var rolerepos: RoleRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = schoolAdminRepo.findByUsername(username)
        return org.springframework.security.core.userdetails.User(user.username, user.getPassword(), user.authority)
    }

    @Throws(EntityNotFoundException::class)
    override fun findAdminById(id: Long): SchoolAdmin {
        return schoolAdminRepo.findById(id)
                .orElseThrow { EntityNotFoundException(java.lang.Long.toString(id)) }
    }

    override fun findAllAdmins(): List<SchoolAdmin> {
        val list = mutableListOf<SchoolAdmin>()
        schoolAdminRepo.findAll().iterator().forEachRemaining{ list.add(it) }
        return list
    }

    override fun deleteAdmin(id: Long) {
        if (schoolAdminRepo.findById(id).isPresent) {
            schoolAdminRepo.deleteById(id)
        } else {
            throw EntityNotFoundException(java.lang.Long.toString(id))
        }
    }

    @Transactional
    override fun saveAdmin(schoolAdmin: SchoolAdmin): SchoolAdmin {
        val newSchoolAdmin = SchoolAdmin()
        newSchoolAdmin.username = schoolAdmin.username
        newSchoolAdmin.setPasswordNoEncrypt(schoolAdmin.getPassword()!!)
        newSchoolAdmin.firstname = schoolAdmin.firstname
        newSchoolAdmin.lastname = schoolAdmin.lastname
        newSchoolAdmin.email = schoolAdmin.email
        newSchoolAdmin.phone = schoolAdmin.phone

        val newRoles = ArrayList<UserRoles>()
        for (ur in schoolAdmin.userRoles) {
            newRoles.add(UserRoles(newSchoolAdmin, ur.role!!))
        }
        newSchoolAdmin.userRoles = newRoles

        return schoolAdminRepo.save(newSchoolAdmin)
    }


    @Transactional
    override fun updateAdmin(schoolAdmin: SchoolAdmin, id: Long): SchoolAdmin {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = schoolAdminRepo.findByUsername(authentication.name)

        if (currentUser != null) {
            if (id == currentUser.userid) {
                if (schoolAdmin.username != null) {
                    currentUser.username = schoolAdmin.username
                }

                if (schoolAdmin.getPassword() != null) {
                    currentUser.setPasswordNoEncrypt(schoolAdmin.getPassword()!!)
                }

                if (schoolAdmin.userRoles.size > 0) {
                    // with so many relationships happening, I decided to go
                    // with old school queries
                    // delete the old ones
                    rolerepos.deleteUserRolesByUserId(currentUser.userid)

                    // add the new ones
                    for (ur in schoolAdmin.userRoles) {
                        rolerepos.insertUserRoles(id, ur.role!!.roleid)
                    }
                }
                return schoolAdminRepo.save(currentUser)
            } else {
                throw EntityNotFoundException(java.lang.Long.toString(id) + " Not current user")
            }
        } else {
            throw EntityNotFoundException(authentication.name)
        }

    }
}
