package com.revature.repositories

import com.revature.models.Reimbursement
import com.revature.p1refactorback.P1RefactorBackApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@SpringBootTest(classes = [P1RefactorBackApplication::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReimbursementDAOTest: AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var reimbDAO: ReimbursementDAOI
    @Autowired
    private lateinit var employeeDAO: EmployeeDAOI

    private val reimbList = listOf(TestUtil.approvedReimb, TestUtil.pendingReimb, TestUtil.deniedReimb)
    private val newReimb = Reimbursement(0,TestUtil.employee,"fakeurl")

    @BeforeMethod
    fun setUp() {
        employeeDAO.save(TestUtil.employee)
        employeeDAO.save(TestUtil.fm)
        reimbDAO.saveAll(reimbList)
    }

    // TODO might not be necessary since we're using H2
    @AfterMethod
    fun tearDown() {
        reimbDAO.deleteAll()
    }

    @Test
    fun testFindById() {
        assertEquals(reimbDAO.findById(1).get(), TestUtil.approvedReimb)
    }

    @Test
    fun testFindByIdInvalid() {
        assertTrue(!reimbDAO.findById(-1).isPresent)
    }

    @Test
    fun testFindAll() {
        var allReimb = reimbDAO.findAll().toHashSet()
        assertEquals(allReimb.size, 3)
    }

    @Test
    fun newReimb() {
        val ret = reimbDAO.save(newReimb)
        assertEquals(ret.id,4)
        assertEquals(4,reimbDAO.findAll().toSet().size)
    }

}