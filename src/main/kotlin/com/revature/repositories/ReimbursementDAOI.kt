package com.revature.repositories

import com.revature.models.Reimbursement
import com.revature.models.enums.SpendingCategory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReimbursementDAOI: CrudRepository<Reimbursement, Int> {
    fun save(r: Reimbursement): Reimbursement
}