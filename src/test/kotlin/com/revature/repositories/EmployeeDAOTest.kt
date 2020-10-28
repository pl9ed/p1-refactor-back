package com.revature.repositories

import com.revature.models.Employee
import com.revature.p1refactorback.P1RefactorBackApplication
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.web.context.WebApplicationContext
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNull
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@SpringBootTest(classes = [P1RefactorBackApplication::class])
class EmployeeDAOTest(): AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var empDAO:EmployeeDAOI

    @BeforeMethod
    private fun methodInit() {
        empDAO.save(TestUtil.employee)
        empDAO.save(TestUtil.fm)
    }

    @AfterMethod
    private fun methodCleanup() {
        empDAO.delete(TestUtil.employee)
        empDAO.delete(TestUtil.fm)
    }

    @Test
    private fun testFindById() {
        assertEquals(TestUtil.employee, empDAO.findByUsername(TestUtil.employee.username))
    }

    @Test
    private fun testFindByIdNonexistent() {
        assertNull(empDAO.findByUsername("nonexistent"))
    }

    @Test
    private fun testSave() {
        val newUser = Employee("username","password")
        assertEquals(newUser, empDAO.save(newUser))
    }

    @Test
    private fun testSaveUpdate() {
        val updatedUser = TestUtil.employee
        val newFN = "NotJohn"
        updatedUser.firstName = newFN

        assertEquals(updatedUser, empDAO.save(updatedUser))

        val retrievedUser = empDAO.findByUsername(updatedUser.username)

        assertEquals(updatedUser, retrievedUser)
        assertEquals(newFN, retrievedUser?.firstName)
    }

}