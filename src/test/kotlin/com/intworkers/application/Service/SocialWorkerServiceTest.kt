package com.intworkers.application.Service

import com.intworkers.application.Application
import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.user.Role
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
import com.intworkers.application.repository.schoolsystem.SchoolRepository
import com.intworkers.application.repository.schoolsystem.SocialWorkerRepository
import com.intworkers.application.service.schoolsystem.SchoolService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.user.UserService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import org.junit.Assert.*
import org.springframework.data.domain.Pageable
import javax.persistence.EntityNotFoundException

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class])
class SocialWorkerServiceTest {

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var schoolService: SchoolService

    @Autowired
    private lateinit var userService: UserService

    @Before
    @Throws(java.lang.Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {

    }

    @Test
    fun findByIdTest() {
        assertEquals("socialworker", socialWorkerService.findById(3).user?.username)
    }

    @Test(expected = ResourceNotFoundException::class)
    fun findByIdNotFoundTest() {
        assertEquals("socialworker", socialWorkerService.findById(2).user?.username)
    }

    @Test
    fun findAllTest() {
        assertEquals("socialworker", socialWorkerService.findAll(Pageable.unpaged()).get(0).user?.username)
    }

    @Test
    fun saveTest() {
        val role2 = Role()
        role2.roleId = 2
        val workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), role2))
        var user3 = User("myaah", "password", workers)
        user3 = userService.save(user3)
        var socialWorker1 = SocialWorker()
        socialWorker1.firstName = "Vivek"
        socialWorker1.lastName = "Vishwanath"
        socialWorker1.email = "socialWorker1@gmail.com"
        socialWorker1.phone = "555-555-5555"
        socialWorker1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        socialWorker1.user = user3
        socialWorker1.workerid = user3.userId
        socialWorkerService.save(socialWorker1)

        assertEquals("myaah", socialWorkerService.findById(4).user?.username)
    }

    @Test
    fun updateTest() {
        val role2 = Role()
        role2.roleId = 2
        val workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), role2))
        var user3 = User("myaah", "password", workers)
        user3 = userService.save(user3)
        var socialWorker1 = SocialWorker()
        socialWorker1.firstName = "Vivek"
        socialWorker1.lastName = "Vishwanath"
        socialWorker1.email = "socialWorker1@gmail.com"
        socialWorker1.phone = "555-555-5555"
        socialWorker1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        socialWorker1.user = user3
        socialWorker1.workerid = user3.userId
        socialWorker1 = socialWorkerService.save(socialWorker1)

        socialWorker1.firstName = "myaah"
        socialWorker1 = socialWorkerService.update(socialWorker1, socialWorker1.workerid)
        assertEquals("myaah", socialWorker1.firstName)
    }

    @Test(expected = ResourceNotFoundException::class)
    fun deleteTest() {
        val role2 = Role()
        role2.roleId = 2
        val workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), role2))
        var user3 = User("myaah", "password", workers)
        user3 = userService.save(user3)
        var socialWorker1 = SocialWorker()
        socialWorker1.firstName = "Vivek"
        socialWorker1.lastName = "Vishwanath"
        socialWorker1.email = "socialWorker1@gmail.com"
        socialWorker1.phone = "555-555-5555"
        socialWorker1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        socialWorker1.user = user3
        socialWorker1.workerid = user3.userId
        socialWorker1 = socialWorkerService.save(socialWorker1)

        socialWorkerService.delete(4)
        socialWorker1 = socialWorkerService.findById(4)
        val i = 0
    }
}