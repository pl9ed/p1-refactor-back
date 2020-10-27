package com.revature.repositories

import com.revature.models.Reimbursement
import com.revature.models.enums.SpendingCategory
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ReimbursementDAOI: CrudRepository<Reimbursement, Int> {
    override fun findById(id:Int): Optional<Reimbursement>
    override fun findAll(): Set<Reimbursement>
    fun save(reimb: Reimbursement): Reimbursement
}