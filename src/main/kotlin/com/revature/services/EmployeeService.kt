package com.revature.services

import com.revature.models.Employee
import org.mindrot.jbcrypt.BCrypt

class EmployeeService {

    fun checkPassword(user: Employee, pass:String):Boolean {
        return BCrypt.checkpw(pass,user.password)
    }

    fun setPassword(user: Employee, pass:String):Boolean {
        user.password = BCrypt.hashpw(pass, BCrypt.gensalt())
        return BCrypt.checkpw(pass,user.password)
    }
}