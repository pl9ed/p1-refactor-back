package com.revature.repositories

import TestUtil
import com.revature.models.Employee
import com.revature.p1refactorback.P1RefactorBackApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.*
import org.testng.annotations.AfterMethod
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
        assertEquals(empDAO.findByUsername(TestUtil.employee.username).get(), TestUtil.employee)
    }

    @Test
    private fun testFindByIdNonexistent() {
        assertTrue(!empDAO.findByUsername("nonexistent").isPresent)
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

        val retrievedOptional = empDAO.findByUsername(updatedUser.username)
        if (retrievedOptional.isPresent) {
            val retrievedUser = retrievedOptional.get()
            assertEquals(updatedUser, retrievedUser)
            assertEquals(newFN, retrievedUser.firstName)
        } else {
            fail("Empty Optional")
        }


    }

}