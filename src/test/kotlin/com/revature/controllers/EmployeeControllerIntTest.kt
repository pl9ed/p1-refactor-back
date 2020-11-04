package com.revature.controllers

import com.revature.p1refactorback.P1RefactorBackApplication
import com.revature.repositories.EmployeeDAOI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

import org.testng.Assert.*

@SpringBootTest(classes = [P1RefactorBackApplication::class])
class EmployeeControllerIntTest: AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var empDAO: EmployeeDAOI

    @BeforeMethod
    fun setUp() {
        empDAO.saveAll(TestUtil.employeeList)
    }

    @AfterMethod
    fun tearDown() {
    }
}