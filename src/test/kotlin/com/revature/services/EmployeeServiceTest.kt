package com.revature.services

import com.revature.models.Employee
import com.revature.repositories.EmployeeDAOI
import org.hamcrest.Matchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import org.testng.Assert.*
import org.testng.annotations.BeforeClass

class EmployeeServiceTest {

    @Mock
    private lateinit var empDAO:EmployeeDAOI

    @InjectMocks
    private val empService: EmployeeService = EmployeeService()
    private val user: Employee = TestUtil.employee



    @BeforeClass
    fun setUpBeforeClass() {
        MockitoAnnotations.initMocks(this)

        `when`(empDAO.findByUsername(TestUtil.employee.username)).thenReturn(TestUtil.employee)
    }

    @BeforeMethod
    fun setUp() {
    }

    @AfterMethod
    fun tearDown() {
    }

    @Test
    fun testCheckPassword() {
        assertNotEquals("pass1", user.password)
        assertTrue(empService.checkPassword(user, "pass1"))
    }

    @Test
    fun testSetPassword() {
        val oldPass:String = user.password
        assertTrue(empService.setPassword(user, "newpass"))
        assertNotEquals(oldPass, user.password)
        assertNotEquals("newpass", user.password)
        assertTrue(empService.checkPassword(user, "newpass"))
    }

    @Test
    fun testUpdateUser() {

        val user = TestUtil.employee
        val newFirstName = "Same"
        user.firstName = newFirstName
        `when`(empDAO.save(user)).thenReturn(user)

        val updatedUser = empService.updateUser(user)

        assertEquals(user, updatedUser)
        assertEquals(newFirstName, updatedUser?.firstName)
    }

    @Test
    fun testUpdateUserNonExistent() {
        val nonexistent = Employee("notreal", "aaa")
        assertNull(empService.updateUser(nonexistent))
    }

    // --------------- HELPER METHODS FOR KOTLIN ------------------------------

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}