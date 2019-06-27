package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auditing.Auditable
import com.intworkers.application.model.user.SocialWorker
import io.swagger.annotations.ApiModel
import javax.persistence.*

@ApiModel(value = "organizations")
@Entity
@Table(name = "organizations")
class Organization (
        @Column
        var organizationName: String? = null,

        @Column(columnDefinition = "TEXT")
        var organizationMission: String? = null,

        @OneToMany(mappedBy = "organization", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("organization")
        var workers: MutableList<SocialWorker> = mutableListOf()
): Auditable() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var organizationId: Long = 0
}