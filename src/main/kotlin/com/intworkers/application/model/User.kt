package com.intworkers.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.ArrayList
import javax.persistence.*

open class User {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        open var userid: Long = 0

        @Column(nullable = false, unique = true)
        open var username: String = ""

        @Column(nullable = false)
        private var password: String = ""

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonIgnoreProperties("user")
        open var userRoles: MutableList<UserRoles> = mutableListOf()

        constructor()

        constructor(name: String, password: String, userRoles: MutableList<UserRoles>) {
                username = name
                setPassword(password)
                for (ur in userRoles) {
                        ur.user = this
                }
                this.userRoles = userRoles
        }

        val authority: List<SimpleGrantedAuthority>
                get() {
                        val rtnList = ArrayList<SimpleGrantedAuthority>()

                        for (r in this.userRoles) {
                                val myRole = "ROLE_" + r.role?.name?.toUpperCase()
                                rtnList.add(SimpleGrantedAuthority(myRole))
                        }

                        return rtnList
                }

        fun getPassword(): String? {
                return password
        }

        fun setPassword(password: String) {
                val passwordEncoder = BCryptPasswordEncoder()
                this.password = passwordEncoder.encode(password)
        }

        fun setPasswordNoEncrypt(password: String) {
                this.password = password
        }
}