package com.intworkers.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auth.User
import javax.persistence.*

@Entity
@Table(name = "visits")
class Visit (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var visitid: Long = 0,

        @ManyToOne
        @JoinColumn(name = "workerid")
        @JsonIgnoreProperties("visits")
        var worker: User? = null,

        @ManyToOne
        @JoinColumn(name = "schoolid")
        @JsonIgnoreProperties("visits")
        var school: School? = null,

        @Column
        var visitdate: String? = null,

        @Column
        var visitreason: String? = null,

        @Column
        var visitdescription: String? = null
)