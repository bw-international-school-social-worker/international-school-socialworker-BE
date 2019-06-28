package com.intworkers.application.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.intworkers.application.controller.auth.OpenController
import com.intworkers.application.model.user.Role
import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.user.RoleService
import com.intworkers.application.service.user.UserService
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.mockito.ArgumentMatchers.any

@RunWith(SpringRunner::class)
@WebMvcTest(value = OpenController::class, secure = false)
@EnableSpringDataWebSupport
class OpenControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var roleService: RoleService

    @MockBean
    private lateinit var schoolAdminService: SchoolAdminService

    @MockBean
    private lateinit var socialWorkerService: SocialWorkerService

    private lateinit var user: User

    private lateinit var socialWorker: SocialWorker

    private lateinit var schoolAdmin: SchoolAdmin

    @Before
    @Throws(Exception::class)
    fun setUp() {

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {

    }

    @Test
    @Throws(Exception::class)
    fun createNewSchoolAdminTest() {
        val apiUrl = "/createnewuser/schooladmin"
        user = User()
        user.userId = 1
        user.username = "myaah"
        user.setPassword("myaah")
        schoolAdmin = SchoolAdmin()
        schoolAdmin.firstName = "Vivek"
        schoolAdmin.user = user
        schoolAdmin.adminId = user.userId

        Mockito.`when`(userService.save(any(User::class.java))).thenReturn(user)

        val rb = MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON)
        val r = mockMvc.perform(rb).andReturn()
        val tr = r.getResponse().getContentAsString()

        val mapper = ObjectMapper()
        val er = mapper.writeValueAsString(schoolAdmin)

        assertEquals("Rest API Returns List", er, tr)
    }

}