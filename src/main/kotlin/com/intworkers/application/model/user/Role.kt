package com.intworkers.application.model.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "roles")
class Role {

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