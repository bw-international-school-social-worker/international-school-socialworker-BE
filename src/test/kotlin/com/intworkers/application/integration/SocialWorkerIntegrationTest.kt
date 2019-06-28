package com.intworkers.application.integration

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocialWorkerIntegrationTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext


    @Before
    fun setUp() {

    }
}