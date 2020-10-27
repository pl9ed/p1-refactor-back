package com.revature.models

import com.revature.models.enums.EmployeeType

class EmployeeDTO (
        val username:String,
        var password:String,
        var type: EmployeeType = EmployeeType.EMPLOYEE,
        var firstName:String? = "",
        var lastName:String? = "") {

    fun toEmployee(): Employee {
        return Employee(this.username,this.password, this.type, this.firstName,this.lastName)
    }
}
