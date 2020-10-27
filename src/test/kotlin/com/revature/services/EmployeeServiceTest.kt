package com.revature.services

import com.revature.models.Employee
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import org.testng.Assert.*

class EmployeeServiceTest {

    private val empService: EmployeeService = EmployeeService()
    private val user: Employee = TestUtil.employee

    @BeforeMethod
    fun setUp() {
    }

    @AfterMethod
    fun tearDown() {
    }

    @Test
    fun testCheckPassword() {
        assertNotEquals("pass1", user.password)
        assertTrue(empService.checkPassword(user,"pass1"))
    }

    @Test
    fun testSetPassword() {
        val oldPass:String = user.password
        assertTrue(empService.setPassword(user,"newpass"))
        assertNotEquals(oldPass, user.password)
        assertNotEquals("newpass",user.password)
        assertTrue(empService.checkPassword(user,"newpass"))
    }
}