package com.intworkers.application.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "schooladmins")
class SchoolAdmin(
        uname: String = "",
        pass: String = "",
        ur: MutableList<UserRoles> = mutableListOf(),

        @Column(nullable = false)
        var firstname: String = "",

        @Column(nullable = false)
        var lastname: String = "",

        @Column(nullable = false)
        var email: String = "",

        @Column(nullable = false)
        var phone: String = ""

        ) : User(uname, pass, ur) {

}