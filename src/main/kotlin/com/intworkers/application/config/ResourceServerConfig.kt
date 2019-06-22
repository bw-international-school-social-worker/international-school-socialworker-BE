package com.intworkers.application.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler

@Configuration
@EnableResourceServer
 class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(RESOURCE_ID).stateless(false)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // http.anonymous().disable();
        http.authorizeRequests().antMatchers(
                "/",
                "/h2-console/**",
                "/swagger-resources/**",
                "/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/security",
                "/swagger-resource/**",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**",
                "/createnewuser"
        )
                .permitAll()
                .antMatchers("/users/**", "/oauth/revoke-token").authenticated()
                .antMatchers("/roles/**").hasAnyRole("ADMIN", "USER", "DATA")
                .antMatchers("/actuator/**").hasAnyRole("ADMIN")
                .and()
                .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())

        // http.requiresChannel().anyRequest().requiresSecure();
        http.csrf().disable()
        http.headers().frameOptions().disable()
    }

    companion object {

        private val RESOURCE_ID = "resource_id"
    }
}
