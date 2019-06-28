package com.intworkers.application.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.intworkers.application.model.schoolsystem.Student
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.hamcrest.number.OrderingComparison.lessThan

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocialWorkerIntegrationTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext


    @Before
    fun setUp() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun findAllResponseTimeTest() {
        given().`when`().get("/students/all").then().time(lessThan(5000L))
    }

    @Test
    fun findByIdResponseTimeTest() {
        given().`when`().get("/students/1").then().time(lessThan(5000L))
    }

    // Couldn't test due to auth issues
    @Test
    @Throws(Exception::class)
    fun createNewStudentTest() {
        val student = Student()
        student.firstName = "Vivek"

        val mapper = ObjectMapper()
        val stringStudent = mapper.writeValueAsString(student)

        given().contentType("application/json")
                .body(stringStudent).`when`()
                .post("/students/new").then().statusCode(201)
    }
}