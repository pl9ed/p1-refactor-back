package com.revature.services

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

import org.testng.Assert.*
import org.testng.annotations.Test

class S3ServiceTest {

    private val baseUrl = "https://pl9ed-s3.s3.amazonaws.com/p1-refactor/"
    private val s3Service = S3Service()
    private val filename = "testFile"
    private val fileContent = byteArrayOf(1,2,3)

    @BeforeMethod
    fun setUp() {

    }

    @AfterMethod
    fun tearDown() {
    }

    @Test
    fun testPut() {
        assertEquals(baseUrl+filename, s3Service.putObject(filename,fileContent))
    }

    @Test(dependsOnMethods= ["testPut"])
    fun testDelete() {
        assertTrue(s3Service.deleteObject(filename))
    }
}