package com.intworkers.application.service

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import java.util.Optional

@Component
class UserAuditing : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        val uname: String
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            uname = authentication.name
        } else {
            uname = "SYSTEM"
        }
        return Optional.of(uname)
    }

}