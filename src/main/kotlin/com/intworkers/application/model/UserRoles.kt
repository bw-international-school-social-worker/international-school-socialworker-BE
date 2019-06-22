package com.intworkers.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "userroles")
class UserRoles : Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties("userRoles")
    var user: User? = null


    @Id
    @ManyToOne
    @JoinColumn(name = "roleid")
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