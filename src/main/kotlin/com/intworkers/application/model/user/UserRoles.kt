package com.intworkers.application.model.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auditing.Auditable
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "userroles")
class UserRoles : Serializable, Auditable {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("userRoles")
    var user: User? = null


    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties("userRoles")
    var role: Role? = null

    constructor() {}

    constructor(user: User, role: Role) {
        this.user = user
        this.role = role
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is UserRoles) {
            return false
        }
        val userRoles = o as UserRoles?
        return user == userRoles!!.user && role == userRoles.role
    }

    override fun hashCode(): Int {
        return Objects.hash(user, role)
    }
}