package com.revature.services


import com.revature.models.Reimbursement
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

    fun uploadReceipt(filename:String, file: ByteArray): String = if (file.isNotEmpty()) {
        s3Service.putObject(filename, file)
    } else {
        ""
    }

    fun deleteReimbursement(id: Int): Boolean {
        val reimb = reimbDAO.findById(id)
        if (reimb.isPresent) {
            if (!deleteReceipt(reimb.get().imageUrl)) {
                return false
            } else {
                reimbDAO.deleteById(id)
            }
        }
        // return true if deleteById or if reimb didnt exist in the first place
        return true
    }

    fun deleteReceipt(url:String): Boolean {
        return try {
            val urlArr = url.split("/")
            val filename = urlArr[urlArr.size-1]
            s3Service.deleteObject(filename)
        } catch (e: IndexOutOfBoundsException) {
            // TODO log
            false
        }
    }

}