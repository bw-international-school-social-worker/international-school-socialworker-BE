package com.intworkers.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableWebMvc
@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
class Application

fun main(args: Array<String>) {
    val ctx = runApplication<Application>(*args)

    val dispatcherServlet = ctx.getBean("dispatcherServlet") as DispatcherServlet
    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true)

}
