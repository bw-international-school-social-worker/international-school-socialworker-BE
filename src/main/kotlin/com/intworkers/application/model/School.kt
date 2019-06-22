package com.intworkers.application.model

import com.intworkers.application.model.auth.User
import javax.persistence.*

@Entity
@Table(name = "schools")
class School(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var schoolid: Long = 0,

        @Column(nullable = false)
        var schoolname: String? = null,

        @Column(nullable = true)
        var dateestablished : String? = null,

        @OneToOne
        var schooladmin: User? = null
)