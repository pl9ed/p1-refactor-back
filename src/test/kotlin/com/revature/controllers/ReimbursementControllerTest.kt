package com.revature.controllers

import TestUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.revature.models.Employee
import com.revature.models.Reimbursement
import com.revature.models.ReimbursementDTO
import com.revature.models.enums.EmployeeType
import com.revature.repositories.EmployeeDAOI
import com.revature.repositories.ReimbursementDAOI
import com.revature.services.ReimbursementService
import io.restassured.module.mockmvc.RestAssuredMockMvc.*
import org.mockito.*
import org.mockito.ArgumentMatchers.eq
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.*

class ReimbursementControllerTest {

    @Mock
    private lateinit var reimbDAO: ReimbursementDAOI

    @Mock
    private lateinit var reimbService: ReimbursementService

    @Mock
    private lateinit var empDAO: EmployeeDAOI

    @InjectMocks
    private val reimbController = ReimbursementController()

    private val om = ObjectMapper()
    private val reimbSet = HashSet<Reimbursement>()
    private val fakeUrlBase = "https://pl9ed-s3.s3.amazonaws.com/p1-refactor/"

    @BeforeClass
    fun setUpBeforeClass() {
        MockitoAnnotations.initMocks(this)
        standaloneSetup(reimbController)
        Mockito.`when`(reimbDAO.findById(0)).thenReturn(Optional.empty())
        Mockito.`when`(reimbDAO.findById(1)).thenReturn(Optional.of(TestUtil.approvedReimb))
        Mockito.`when`(reimbDAO.findById(2)).thenReturn(Optional.of(TestUtil.pendingReimb))
        Mockito.`when`(reimbDAO.findById(3)).thenReturn(Optional.of(TestUtil.deniedReimb))
        Mockito.`when`(reimbDAO.findAll()).thenReturn(reimbSet)
        Mockito.`when`(empDAO.findByUsername("user2")).thenReturn(Optional.of(TestUtil.fm))

        reimbSet.add(TestUtil.approvedReimb)
        reimbSet.add(TestUtil.pendingReimb)
        reimbSet.add(TestUtil.deniedReimb)
    }

    @Test
    fun testGetReimb() {
        var response = get("/reimbursement/1")

        var expectedString = om.writeValueAsString(TestUtil.approvedReimb)

        assertEquals(response.statusCode, 200)
        assertEquals(response.body().asString(), expectedString)

        response = get("/reimbursement/3")
        expectedString = om.writeValueAsString(TestUtil.deniedReimb)

        assertEquals(response.statusCode, 200)
        assertEquals(response.body().asString(), expectedString)
    }

    @Test
    fun testGetReimbNoContent() {
        get("/reimbursement/-1")
                .then()
                .log().ifValidationFails()
                .statusCode(204)
    }

    @Test
    fun testGetReimbBadFormat() {
        get("/reimbursement/abc")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
    }

    @Test
    fun testGetAllReimb() {
        val response = get("/reimbursement")
        val body = response.body().asString()
        val expectedBody = om.writeValueAsString(reimbSet)

        assertEquals(body, expectedBody)
    }

    @Test
    fun testPost() {
        val fileName = "test.png"
        val newReimbDTO = ReimbursementDTO(TestUtil.employee)
        newReimbDTO.amount = 25.0
        val newReimb = newReimbDTO.toReimbursement("fakeurl.com/img")
        newReimb.id = 1

        var reimbString = om.writeValueAsString(newReimbDTO)

        Mockito.`when`(reimbDAO.save(any(Reimbursement::class.java))).thenReturn(newReimb)
        Mockito.`when`(reimbService.uploadReceipt(any(String::class.java), any(ByteArray::class.java)))
                .thenReturn(fakeUrlBase+fileName)
        val response = given()
                .param("reimbursementDTO", reimbString)
                .multiPart("file",fileName,byteArrayOf(1,2,3))
                .`when`()
                .post("/reimbursement")

        reimbString = om.writeValueAsString(newReimb)

        assertEquals(response.statusCode, 201)
        assertEquals(response.body.asString(),reimbString)
    }



    @Test
    fun testPostIncorrectJSON() {
        val fileName = "test.png"
        val newReimbDTO = ReimbursementDTO(TestUtil.employee)
        newReimbDTO.amount = 25.0
        val newReimb = newReimbDTO.toReimbursement("fakeurl.com/img")
        newReimb.id = 1

        Mockito.`when`(reimbDAO.save(any(Reimbursement::class.java))).thenReturn(newReimb)
        Mockito.`when`(reimbService.uploadReceipt(any(String::class.java), any(ByteArray::class.java)))
                .thenReturn(fakeUrlBase+fileName)

        var reimbString = "{aaa" + om.writeValueAsString(newReimbDTO)
        val response = given()
                .param("reimbursementDTO", reimbString)
                .multiPart("file",fileName,byteArrayOf(1,2,3))
                .`when`()
                .post("/reimbursement")

        assertEquals(response.statusCode, 400)
    }

    @Test
    fun testPostMissingFile() {
        val fileName = "test.png"
        val newReimbDTO = ReimbursementDTO(TestUtil.employee)
        newReimbDTO.amount = 25.0
        val newReimb = newReimbDTO.toReimbursement("fakeurl.com/img")
        newReimb.id = 1

        Mockito.`when`(reimbDAO.save(any(Reimbursement::class.java))).thenReturn(newReimb)
        Mockito.`when`(reimbService.uploadReceipt(any(String::class.java), any(ByteArray::class.java)))
                .thenReturn(fakeUrlBase+fileName)

        var reimbString = om.writeValueAsString(newReimbDTO)
        val response = given()
                .param("reimbursementDTO", reimbString)
                .`when`()
                .post("/reimbursement")

        assertEquals(response.statusCode, 415)
    }

    @Test
    fun testPostEmptyFile() {
        val fileName = "test.png"
        val newReimbDTO = ReimbursementDTO(TestUtil.employee)
        newReimbDTO.amount = 25.0
        val newReimb = newReimbDTO.toReimbursement("fakeurl.com/img")
        newReimb.id = 1

        Mockito.`when`(reimbDAO.save(any(Reimbursement::class.java))).thenReturn(newReimb)
        Mockito.`when`(reimbService.uploadReceipt(any(String::class.java), any(ByteArray::class.java)))
                .thenReturn(fakeUrlBase+fileName)

        var reimbString = om.writeValueAsString(newReimbDTO)
        val response = given()
                .param("reimbursementDTO", reimbString)
                .multiPart("file",null, byteArrayOf())
                .`when`()
                .post("/reimbursement")

        assertEquals(response.statusCode, 400)
        assertEquals(response.headers.getValue("cause"),"Missing receipt")
    }

    @Test
    fun testDelete() {
        Mockito.`when`(reimbService.deleteReimbursement(1)).thenReturn(true)
        Mockito.`when`(reimbService.deleteReimbursement(99)).thenReturn(false)

        val response = delete("/reimbursement/1")
        assertEquals(response.statusCode,200)
        assertEquals(response.body.asString(), "true")
    }

    @Test
    fun testDeleteNone() {
        Mockito.`when`(reimbService.deleteReimbursement(99)).thenReturn(false)

        val response = delete("/reimbursement/99")
        assertEquals(response.statusCode,200)
        assertEquals(response.body.asString(), "false")
    }

    @Test
    fun testUpdate() {
        val reimb1App = TestUtil.pendingReimb
        reimb1App.resolver = TestUtil.fm
        reimb1App.status = 1
        Mockito.`when`(reimbDAO.save(reimb1App)).thenReturn(reimb1App)

        val json = om.writeValueAsString(reimb1App)

        val response = given()
                .body(json)
                .contentType("application/json")
                .put("/reimbursement/${reimb1App.id}")

        val body = om.readValue(response.body.asString(), Reimbursement::class.java)

        assertEquals(response.statusCode,200)
        assertEquals(body, reimb1App)
    }

    @Test
    fun testUpdateInvalidResolver() {
        val reimb1App = TestUtil.pendingReimb
        reimb1App.resolver = TestUtil.employee
        reimb1App.status = 1
        Mockito.`when`(reimbDAO.save(reimb1App)).thenReturn(reimb1App)

        val json = om.writeValueAsString(reimb1App)

        val response = given()
                .body(json)
                .contentType("application/json")
                .put("/reimbursement/${reimb1App.id}")

        assertEquals(response.statusCode,400)
        assertEquals(response.headers().getValue("cause"),"Invalid resolver")
    }

    @Test
    fun testUpdateNotFoundResolver() {
        val reimb1App = TestUtil.pendingReimb.copy()
        reimb1App.resolver = Employee("notreal","pass",type=EmployeeType.FINANCE_MANAGER)
        reimb1App.status = 1
        Mockito.`when`(reimbDAO.save(reimb1App)).thenReturn(reimb1App)
        Mockito.`when`(empDAO.findByUsername("notreal")).thenReturn(Optional.empty())

        val json = om.writeValueAsString(reimb1App)

        val response = given()
                .body(json)
                .contentType("application/json")
                .put("/reimbursement/${reimb1App.id}")

        assertEquals(response.statusCode,400)
        assertEquals(response.headers().getValue("cause"),"Invalid resolver")
    }

    // --------------- HELPER FUNCTIONS ------------------

    private fun <T> any(type : Class<T>): T {
        Mockito.any(type)
        return null as T
    }
}