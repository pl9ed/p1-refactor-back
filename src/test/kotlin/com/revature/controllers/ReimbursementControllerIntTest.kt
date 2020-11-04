package com.revature.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.revature.models.Employee
import com.revature.models.Reimbursement
import com.revature.models.ReimbursementDTO
import com.revature.p1refactorback.P1RefactorBackApplication
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.DependsOn
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.web.context.WebApplicationContext
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

import org.testng.Assert.*
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@SpringBootTest(classes = [P1RefactorBackApplication::class])
class ReimbursementControllerIntTest: AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var reimbController: ReimbursementController

    @Autowired
    private lateinit var wac: WebApplicationContext

    private val om = ObjectMapper()

    @BeforeMethod
    fun initWAC() {
        RestAssuredMockMvc.webAppContextSetup(wac)
    }

    @Test
    fun testPostInvalidSubmitter() {
        val fileName = "test.png"
        val newReimbDTO = ReimbursementDTO(Employee("fake_employee","fakepass"))
        newReimbDTO.amount = 25.0
        val newReimb = newReimbDTO.toReimbursement("fakeurl.com/img")
        newReimb.id = 1

        var reimbString = om.writeValueAsString(newReimbDTO)

        val response = RestAssuredMockMvc.given()
                .param("reimbursementDTO", reimbString)
                .multiPart("file",fileName,byteArrayOf(1,2,3))
                .`when`()
                .post("/reimbursement")

        assertEquals(response.statusCode, 400)
    }
}