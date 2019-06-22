package com.intworkers.application.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "schooladmins")
class SchoolAdmin(
        override var username: String,
        pass: String,
        override var userRoles: MutableList<UserRoles>,

        @Column(nullable = false)
        var firstname: String,

        @Column(nullable = false)
        var lastname: String,

        @Column(nullable = false)
        var email: String,

        @Column(nullable = false)
        var phone: String

        ) : User(username, pass, userRoles) {

}