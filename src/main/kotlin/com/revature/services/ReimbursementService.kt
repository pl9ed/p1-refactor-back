package com.revature.services


import com.revature.repositories.ReimbursementDAOI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Service
class ReimbursementService {

    @Autowired
    private lateinit var s3Service: S3Service

    @Autowired
    private lateinit var reimbDAO: ReimbursementDAOI

    fun uploadReceipt(filename:String, file: ByteArray): String = s3Service.putObject(filename, file);

}