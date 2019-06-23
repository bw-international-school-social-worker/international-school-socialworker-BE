package com.intworkers.application.service.user

import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
import com.intworkers.application.repository.user.RoleRepository
import com.intworkers.application.repository.user.UserRepository
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
    lateinit var userrepos: UserRepository

    @Autowired
    lateinit var rolerepos: RoleRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userrepos.findByUsername(username)
        return org.springframework.security.core.userdetails.User(user.username!!, user.getPassword()!!, user.authority)
    }

    @Throws(EntityNotFoundException::class)
    override fun findUserById(id: Long): User {
        return userrepos.findById(id).orElseThrow { EntityNotFoundException(java.lang.Long.toString(id)) }
    }

    override fun findAll(): List<User> {
        val list = ArrayList<User>()
        userrepos.findAll().iterator().forEachRemaining{ list.add(it) }
        return list
    }

    override fun delete(id: Long) {
        if (userrepos.findById(id).isPresent) {
            userrepos.deleteById(id)
        } else {
            throw EntityNotFoundException(java.lang.Long.toString(id))
        }
    }

    @Transactional
    override fun save(user: User): User {
        val newUser = User()
        newUser.username = user.username
        newUser.setPasswordNoEncrypt(user.getPassword()!!)
        val newRoles = ArrayList<UserRoles>()
        for (ur in user.userRoles) {
            newRoles.add(UserRoles(newUser, ur.role!!))
        }
        newUser.userRoles = newRoles
        newUser.firstName = user.firstName
        newUser.lastName = user.lastName
        newUser.phone = user.phone
        newUser.email = user.email
        newUser.photoUrl = user.photoUrl
        return userrepos.save(newUser)
    }


    @Transactional
    override fun update(user: User, id: Long): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = userrepos.findByUsername(authentication.name)

        if (currentUser != null) {
            if (id == currentUser.userId) {
                if (user.username != null) {
                    currentUser.username = user.username
                }

                if (user.getPassword() != null) {
                    currentUser.setPasswordNoEncrypt(user.getPassword()!!)
                }

                if (user.userRoles.size > 0) {
                    // with so many relationships happening, I decided to go
                    // with old school queries
                    // delete the old ones
                    rolerepos.deleteUserRolesByUserId(currentUser.userId)

                    // add the new ones
                    for (ur in user.userRoles) {
                        rolerepos.insertUserRoles(id, ur.role!!.roleId)
                    }
                }
                if (user.firstName != null) currentUser.firstName = user.firstName
                if (user.lastName != null) currentUser.lastName = user.lastName
                if (user.email != null) currentUser.email = user.email
                if (user.phone != null) currentUser.phone = user.phone
                if (user.photoUrl != null) currentUser.photoUrl = user.photoUrl
                return userrepos.save(currentUser)
            } else {
                throw EntityNotFoundException(java.lang.Long.toString(id) + " Not current user")
            }
        } else {
            throw EntityNotFoundException(authentication.name)
        }

    }
}
