package com.revature.models

import com.revature.models.enums.EmployeeType
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Employee (
        @Id
        val username:String,
        var password:String,
        var type: EmployeeType = EmployeeType.EMPLOYEE,
        var firstName:String? = "",
        var lastName:String? = "") {}
