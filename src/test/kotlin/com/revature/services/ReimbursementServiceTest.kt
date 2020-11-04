package com.revature.services

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

import org.testng.annotations.BeforeClass

class ReimbursementServiceTest {

    @Mock
    private lateinit var s3: S3Service
    @InjectMocks
    private var rService = ReimbursementService()

    private val validFilename = "validFilename"
    private val validFile = ByteArray(10)

    private val invalidFilename = "invalid"
    private val invalidFile = ByteArray(0)

    @BeforeClass
    fun setUpBeforeClass() {
        MockitoAnnotations.initMocks(this)

        // method actually returns hash, just need to check if it isn't empty
        `when`(s3.putObject(validFilename, validFile)).thenReturn("success")
        `when`(s3.putObject(invalidFilename, invalidFile)).thenReturn("")
    }

    @Test
    fun testUploadReceipt() {
        assertTrue(rService.uploadReceipt(validFilename, validFile).isNotEmpty())
    }

    @Test
    fun testUploadReceiptInvalid() {
        assertTrue(rService.uploadReceipt("invalid", invalidFile).length == 0)
    }
}