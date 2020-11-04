package com.revature.models

import com.revature.models.enums.SpendingCategory

class ReimbursementDTO() {
    var submitter: Employee? = null
    var resolver: Employee? = null
    var amount:Double = 0.0
    var description:String = ""
    var category: SpendingCategory = SpendingCategory.OTHER
    var status:Int = 0

    constructor(
            submitter: Employee,
            resolver: Employee? = null,
            amount:Double = 0.0,
            description:String = "",
            category: SpendingCategory = SpendingCategory.OTHER,
            status:Int = 0
    ): this() {
        this.submitter = submitter
        this.resolver = resolver
        this.amount = amount
        this.description = description
        this.category = category
        this.status=status
    }

    fun toReimbursement(imageUrl: String): Reimbursement = Reimbursement(-1, submitter!!, imageUrl, resolver, amount, description, category, status)
}