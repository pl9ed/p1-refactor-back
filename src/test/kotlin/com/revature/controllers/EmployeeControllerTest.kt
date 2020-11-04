package com.revature.controllers

import TestUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.revature.models.Employee
import com.revature.repositories.EmployeeDAOI
import com.revature.services.EmployeeService
import io.restassured.module.mockmvc.RestAssuredMockMvc.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.*

class EmployeeControllerTest {

    @Mock
    private lateinit var empDAO: EmployeeDAOI
    @Mock
    private lateinit var empService: EmployeeService
    @InjectMocks
    private lateinit var empController:EmployeeController

    private val objectMapper = ObjectMapper()

    private val nonexistentUsername = "nonexistent"

    private val invalid = Employee("","temp")


    @BeforeClass
    fun setUpBeforeClass() {
        MockitoAnnotations.initMocks(this)
        standaloneSetup(empController)
        `when`(empDAO.findByUsername(TestUtil.employee.username)).thenReturn(Optional.of(TestUtil.employee))
        `when`(empDAO.findByUsername(nonexistentUsername)).thenReturn(Optional.empty())

        `when`(empDAO.save(TestUtil.employee)).thenReturn(TestUtil.employee)
    }

    @BeforeMethod
    fun setUp() {
    }

    @AfterMethod
    fun tearDown() {
    }

    @Test
    fun testGetEmployee() {
        get("/employee/${TestUtil.employee.username}")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun testGetEmployeeNone() {
        get("/employee/$nonexistentUsername")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(404)
    }

    @Test
    fun testPostEmployee() {
        `when`(empService.isValidEmployee(TestUtil.employee)).thenReturn(true)

        val json = objectMapper.writeValueAsString(TestUtil.employee)
        println(json)
        given()
                .body(json)
                .contentType("application/json")
                .post("/employee")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
    }

    @Test
    fun testPostEmployeeNull() {
        val json = objectMapper.writeValueAsString(null)

        given()
                .body(json)
                .contentType("application/json")
                .post("/employee")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
    }

    @Test
    fun testPostEmployeeInvalid() {
        `when`(empService.isValidEmployee(invalid)).thenReturn(false)

        val json = objectMapper.writeValueAsString(invalid)

        given()
                .body(json)
                .contentType("application/json")
                .post("/employee")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
    }

    @Test
    fun testIncorrectMapping() {
        val json = "{aaa,asdwdwdwde3}"

        given ()
                .body(json)
                .contentType("application/json")
                .post("/employee")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
    }

}