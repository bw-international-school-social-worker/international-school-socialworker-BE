package com.intworkers.application.model

import javax.persistence.*

@Entity
@Table(name = "schooladmins")
class SchoolAdmin(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        override var username: String = "",
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

        ) : User(username, pass, ur)