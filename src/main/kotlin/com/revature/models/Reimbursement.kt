package com.revature.models

import com.revature.models.enums.SpendingCategory
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Reimbursement(@Id @GeneratedValue var id:Int,
                         @ManyToOne
                         var submitter: Employee,
                         var imageUrl:String,
                         @ManyToOne
                         var resolver: Employee? = null,
                         var amount:Double = 0.0,
                         var description:String = "",
                         var category: SpendingCategory = SpendingCategory.OTHER,
                         var status: Int = 0,
                         var submitDate: Long = System.currentTimeMillis(),
                         var resolveDate: Long = -1
                         ) {}
