package com.intworkers.application.controller.auth

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

import javax.servlet.http.HttpServletRequest

@Component
@Controller
class LogoutController {
    @Autowired
    private lateinit var tokenStore: TokenStore

    @ApiOperation(value = "Logout current User", response = Void::class)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SCHOOLADMIN', 'ROLE_SOCIALWORKER')")
    @RequestMapping(value = ["/oauth/revoke-token"], method = [RequestMethod.GET])
    @ResponseStatus(HttpStatus.OK)
    fun logout(request: HttpServletRequest) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null) {
            val tokenValue = authHeader.replace("Bearer", "").trim { it <= ' ' }
            val accessToken = tokenStore.readAccessToken(tokenValue)
            tokenStore.removeAccessToken(accessToken)
        }
    }
}
