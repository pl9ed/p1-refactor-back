package com.revature.controllers

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.revature.models.Reimbursement
import com.revature.models.ReimbursementDTO
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
            val receiptName = "${reimbDTO.submitter?.username}_${System.currentTimeMillis()}" +
                    "_${receipt.originalFilename ?: "_no_filename"}"

            val receiptBytes = receipt.bytes
            val receiptUrl = reimbService.uploadReceipt(receiptName,receiptBytes)

            val reimb = reimbDTO.toReimbursement(receiptUrl)
            val responseBody = reimbDAO.save(reimb)
            ResponseEntity.status(201).body(responseBody)
        } catch (e: JsonParseException) {
            val headers = HttpHeaders()
            headers.add("cause", "Invalid JSON format")
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
}