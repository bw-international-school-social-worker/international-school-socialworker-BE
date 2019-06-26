package com.intworkers.application

import com.intworkers.application.model.schoolsystem.*
import com.intworkers.application.model.user.*
import com.intworkers.application.service.schoolsystem.*
import com.intworkers.application.service.user.RoleService
import com.intworkers.application.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class UserSeedData : CommandLineRunner {

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @Autowired
    private lateinit var schoolService: SchoolService

    @Autowired
    private lateinit var classService: ClassService

    @Autowired
    private lateinit var studentService: StudentService

    @Autowired
    private lateinit var gradeService: GradeService

    @Autowired
    private lateinit var visitService: VisitService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val r1 = Role("schooladmin")
        roleService.save(r1)
        val r2 = Role("socialworker")
        roleService.save(r2)
        val r3 = Role("admin")
        roleService.save(r3)

        // Admin Account
        val role1 = Role()
        role1.roleId = 1
        val role2 = Role()
        role2.roleId = 2
        val role3 = Role()
        role3.roleId = 3
        val admins = mutableListOf<UserRoles>()
        admins.add(UserRoles(User(), role1))
        admins.add(UserRoles(User(), role2))
        admins.add(UserRoles(User(), role3))
        var user3 = User("admin", "password", admins)
        user3 = userService.save(user3)

        // School Admin account
        var schooladmins = mutableListOf<UserRoles>()
        schooladmins.add(UserRoles(User(), r1))
        var user1 = User("schooladmin", "password", schooladmins)
        user1 = userService.save(user1)
        var schoolAdmin1 = SchoolAdmin()
        schoolAdmin1.user = user1
        schoolAdmin1.adminId = user1.userId
        schoolAdmin1.firstName = "Vivek"
        schoolAdmin1.lastName = "Vishwanath"
        schoolAdmin1.email = "schoolAdmin1@gmail.com"
        schoolAdmin1.phone = "555-555-5555"
        schoolAdmin1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        schoolAdmin1 = schoolAdminService.save(schoolAdmin1)

        // Social Worker account
        var workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), r2))
        var user2 = User("socialworker", "password", workers)
        user2 = userService.save(user2)
        var socialWorker1 = SocialWorker()
        socialWorker1.firstName = "Vivek"
        socialWorker1.lastName = "Vishwanath"
        socialWorker1.email = "socialWorker1@gmail.com"
        socialWorker1.phone = "555-555-5555"
        socialWorker1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_" +
                "10209757671297706_6479721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWv" +
                "QCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=sc" +
                "ntent-ort2-2.xx&oh=1fd8f96a7ed4aca2731c9f75356c3843&oe=5D78E841"
        socialWorker1.user = user2
        socialWorker1.workerid = user2.userId
        socialWorkerService.save(socialWorker1)

        // New School
        var school1 = School()
        school1.schoolName = "Vivek's Elementary School"
        school1.dateEstablished = "02/10/1995"
        school1.schoolAdmin = schoolAdmin1
        school1 = schoolService.save(school1)

        //New Class
        var class1 = Course()
        class1.className = "Algebra"
        class1.school = school1
        class1 = classService.save(class1)

        // New Grade
        var grade1 = Grade()
        grade1.gradeNumber = 5
        grade1.school = school1
        grade1 = gradeService.save(grade1)

        // Creates 11 new students
        for (i in 1..10) {
            var student1 = Student()
            student1.firstName = "Vivek"
            student1.lastName = "Vishwanath"
            student1.photoUrl = "https://scontent-ort2-2.xx.fbcdn.net/v/t1.0-9/34818901_10209757671297706_647" +
                    "9721446129008640_n.jpg?_nc_cat=107&_nc_oc=AQkSKMn18y4WZWvQCSCWx9fCDh2RwXpEr2dlrANG60CpX_Dn" +
                    "-aGxdRhaZcRPJV0VMDVsb8SpX97bg-A59qau6L4X&_nc_ht=scontent-ort2-2.xx&oh=1fd8f96a7ed4aca" +
                    "2731c9f75356c3843&oe=5D78E841"
            student1.insuranceExpiration = "02/10/2020"
            student1.specialNeeds = "No special needs"
            student1.backgroundStory = "Comes from an impoverished family and wants to be first in generation to go to college"
            student1.hasInsurance = true
            student1.hasBirthCertificate = true
            student1.contactInfo = "Contact info needs to be updated"
            student1.status = "Currently enrolled student"
            student1.age = 10
            student1.school = school1
            student1.grade = grade1
            student1.studentClass = class1
            student1.worker = socialWorker1
            studentService.save(student1)
        }

        socialWorkerService.assignWorkerToSchool(socialWorker1.workerid, school1.schoolId)
        var visit1 = Visit()
        visit1.visitDate = "02/10/2021"
        visit1.visitDescription = "Worker met many students and teachers and had some students assigned to worker"
        visit1.visitReason = "Standard meet and greet"
        visit1.worker = socialWorker1
        visit1.school = school1
        visit1 = visitService.save(visit1)
    }
}