package com.intworkers.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val ctx = runApplication<Application>(*args)

    val dispatcherServlet = ctx.getBean("dispatcherServlet") as DispatcherServlet
    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true)

}
