package com.intworkers.application.model.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auditing.Auditable
import io.swagger.annotations.ApiModel
import javax.persistence.*

@ApiModel(value = "roles")
@Entity
@Table(name = "roles")
class Role: Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var roleId: Long = 0

    @Column(nullable = false, unique = true)
    var name: String? = ""

    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL])
    @JsonIgnoreProperties("role")
    var userRoles: MutableList<UserRoles> = mutableListOf()

    constructor() {}

    constructor(name: String) {
        this.name = name
    }
}