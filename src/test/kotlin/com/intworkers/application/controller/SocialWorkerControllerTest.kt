package com.intworkers.application.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.intworkers.application.controller.schoolsystem.SocialWorkerController
import com.intworkers.application.model.user.Role
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SchoolService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.user.UserService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Pageable
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.junit.Assert.*
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@RunWith(SpringRunner::class)
@WebMvcTest(value = SocialWorkerController::class, secure = false)
@EnableSpringDataWebSupport
class SocialWorkerControllerTest {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @MockBean
    private lateinit var socialWorkerService: SocialWorkerService

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var schoolService: SchoolService

    @MockBean
    private lateinit var schoolAdminService: SchoolAdminService

    private lateinit var socialWorkerList: MutableList<SocialWorker>

    private lateinit var user1: User

    private lateinit var socialWorker1: SocialWorker

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
        socialWorkerList = mutableListOf()

        val r1 = Role("schooladmin")
        val r2 = Role("socialworker")
        val r3 = Role("admin")

        var workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), r2))
        user1 = User("socialworker", "password", workers)
        user1.userId = 1
        socialWorker1 = SocialWorker()
        socialWorker1.firstName = "Vivek"
        socialWorker1.lastName = "Vishwanath"
        socialWorker1.email = "socialWorker1@gmail.com"
        socialWorker1.phone = "555-555-5555"
        socialWorker1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        socialWorker1.user = user1
        socialWorker1.workerid = user1.userId

        workers = mutableListOf()
        workers.add(UserRoles(User(), r2))
        var user2 = User("socialworker", "password", workers)
        val socialWorker2 = SocialWorker()
        socialWorker2.firstName = "Vivek"
        socialWorker2.lastName = "Vishwanath"
        socialWorker2.email = "socialWorker1@gmail.com"
        socialWorker2.phone = "555-555-5555"
        socialWorker2.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        socialWorker2.user = user2
        socialWorker2.workerid = user2.userId

        socialWorkerList.add(socialWorker1)
        socialWorkerList.add(socialWorker2)

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun findAll() {
        val apiUrl = "/socialworkers/all?page=0&size=5"
        Mockito.`when`(socialWorkerService.findAll(PageRequest(0, 5))).thenReturn(socialWorkerList)

        val rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON)
        val r = mockMvc.perform(rb).andReturn()
        val tr = r.response.contentAsString

        val mapper = ObjectMapper()
        val er = mapper.writeValueAsString(socialWorkerList)
        assertEquals(er, tr)
    }

    @Test
    @Throws(Exception::class)
    fun findById() {
        val apiUrl = "/socialworkers/worker/0"
        Mockito.`when`(socialWorkerService.findById(0)).thenReturn(socialWorkerList[0])

        val rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON)
        val r = mockMvc.perform(rb).andReturn()
        val tr = r.response.contentAsString

        val mapper = ObjectMapper()
        val er = mapper.writeValueAsString(socialWorkerList[0])
        assertEquals(er, tr)
    }

    @Test
    @WithMockUser(username = "socialworker", password = "password", roles = ["socialworker"])
    @WithUserDetails("socialWorker")
    @Throws(Exception::class)
    fun currentWorkerInfo() {
        val auth = SecurityContextHolder.getContext().authentication
        val apiUrl = "/socialworkers/myinfo"
        Mockito.`when`(userService.findByUsername("socialworker")).thenReturn(user1)
        Mockito.`when`(socialWorkerService.findById(user1.userId)).thenReturn(socialWorkerList[0])
        val rb = MockMvcRequestBuilders.get(apiUrl).with(authentication(auth)).accept(MediaType.APPLICATION_JSON)
        val r = mockMvc.perform(rb).andReturn()
        val tr = r.response.contentAsString

        val mapper = ObjectMapper()
        val er = mapper.writeValueAsString(socialWorkerList[0])
        assertEquals(er, tr)
    }

}