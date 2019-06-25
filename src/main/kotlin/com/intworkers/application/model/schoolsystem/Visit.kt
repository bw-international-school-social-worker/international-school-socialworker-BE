package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import javax.persistence.*

@Entity
@Table(name = "visits")
class Visit (
        @ManyToOne
        @JoinColumn(name = "workerId", referencedColumnName = "workerId")
        @JsonIgnore
        var worker: SocialWorker? = null,

        @Column(name = "workerId", insertable = false, updatable = false)
        var workerId: Long = 0,

        @ManyToOne
        @JoinColumn(name = "schoolId", referencedColumnName = "schoolId")
        @JsonIgnore
        var school: School? = null,

        @Column(name = "schoolId", insertable = false, updatable = false)
        var schoolId: Long = 0,

        @Column
        var visitDate: String? = null,

        @Column
        var visitReason: String? = null,

        @Column
        var visitDescription: String? = null
) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var visitId: Long = 0
}