package com.intworkers.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auth.User
import javax.persistence.*

@Entity
@Table(name = "schools")
class School(
        @Column(nullable = false)
        var schoolname: String? = null,

        @Column(nullable = true)
        var dateestablished : String? = null,

        @OneToOne
        var schooladmin: User? = null,

        @ManyToMany(mappedBy = "schools")
        @JsonIgnoreProperties("schools")
        var workers: MutableList<User> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var schoolid: Long = 0
}