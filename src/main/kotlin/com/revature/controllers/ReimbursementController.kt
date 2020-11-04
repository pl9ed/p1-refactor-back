package com.revature.controllers

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.revature.models.Reimbursement
import com.revature.models.ReimbursementDTO
import com.revature.models.enums.EmployeeType
import com.revature.repositories.EmployeeDAOI
import com.revature.repositories.ReimbursementDAOI
import com.revature.services.ReimbursementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ReimbursementController {

    @Autowired
    private lateinit var reimbService: ReimbursementService

    @Autowired
    private lateinit var reimbDAO: ReimbursementDAOI

    @Autowired
    private lateinit var empDAO: EmployeeDAOI

    private val objectMapper = ObjectMapper().registerKotlinModule()

    @GetMapping("/reimbursement/{id}")
    fun getReimbursement(@PathVariable id:Int): ResponseEntity<Reimbursement> {
        try {
            val ret = reimbDAO.findById(id)
            if (ret.isPresent()) {
                return ResponseEntity.ok(ret.get())
            }
            return ResponseEntity.noContent().build()
        }
        catch (e: NumberFormatException) {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/reimbursement")
    fun getAllReimbursements(): ResponseEntity<MutableIterable<Reimbursement>> = ResponseEntity.ok(reimbDAO.findAll())

    @PostMapping("/reimbursement", consumes = ["multipart/form-data"])
    fun postReimbursement(@RequestParam("reimbursementDTO") reimbDTOString: String,
                          @RequestParam("file") receipt: MultipartFile): ResponseEntity<Reimbursement> {
        return try {
            val reimbDTO = objectMapper.readValue(reimbDTOString, ReimbursementDTO::class.java)
            val receiptName = StringBuilder().append("${reimbDTO.submitter?.username}_${System.currentTimeMillis()}")

            var fileName = receipt.originalFilename
            if (fileName.isNullOrEmpty()) {
                fileName = "_no_filename"
            }
            receiptName.append(fileName)

            val receiptBytes = receipt.bytes
            if (receiptBytes.isEmpty()) {
                val headers = HttpHeaders()
                headers.add("cause", "Missing receipt")
                return ResponseEntity.badRequest().headers(headers).build()
            }
            val receiptUrl = reimbService.uploadReceipt(receiptName.toString(),receiptBytes)

            val reimb = reimbDTO.toReimbursement(receiptUrl)
            val responseBody = reimbDAO.save(reimb)
            ResponseEntity.status(201).body(responseBody)
        } catch (e: JsonParseException) {
            val headers = HttpHeaders()
            headers.add("cause", "Invalid JSON")
            ResponseEntity.badRequest().headers(headers).build()
        } catch (e: JpaObjectRetrievalFailureException) {
            val headers = HttpHeaders()
            headers.add("cause", "Invalid submitter or resolver")
            ResponseEntity.status(400).headers(headers).build()
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).build()
        }
    }

    @PutMapping("/reimbursement/{id}")
    fun updateStatus(@RequestBody reimb: Reimbursement): ResponseEntity<Reimbursement> {
        val headers = HttpHeaders()
        return try {
            if (reimb.resolver?.type == EmployeeType.FINANCE_MANAGER) {
                val resolverExists = empDAO.findByUsername(reimb.resolver!!.username).isPresent
                if (resolverExists) {
                    val body = reimbDAO.save(reimb)
                    return ResponseEntity.ok(body)
                }
            }
            headers.add("cause","Invalid resolver")
            ResponseEntity.badRequest().headers(headers).build()
        } catch (e: JsonParseException) {
            headers.add("cause", "Invalid JSON")
            ResponseEntity.badRequest().headers(headers).build()
        } catch (e: JsonMappingException) {
            headers.add("cause","Invalid resolver")
            ResponseEntity.badRequest().headers(headers).build()
        }
    }

    @DeleteMapping("/reimbursement/{id}")
    fun deleteReimbursement(@PathVariable id: Int): ResponseEntity<Boolean> =
        ResponseEntity.ok(reimbService.deleteReimbursement(id))
}