package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.user.SocialWorker
import javax.persistence.*

@Entity
@Table(name = "organizations")
class Organization (
        @Column
        var organizationName: String? = null,

        @Column
        var organizationMission: String? = null,

        @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("organization")
        var workers: MutableList<SocialWorker> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var organizationId: Long = 0
}