package com.revature.models

import com.revature.models.enums.SpendingCategory
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Reimbursement(@Id val id:Int,
                         @ManyToOne
                         var submitter: Employee,
                         var imageUrl:String,
                         @ManyToOne
                         var resolver: Employee? = null,
                         var amount:Double = 0.0,
                         var description:String = "",
                         var category: SpendingCategory = SpendingCategory.OTHER
                         ) {}
