package com.revature.services

import com.revature.models.Employee
import com.revature.repositories.EmployeeDAOI
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class EmployeeService {

    @Autowired
    private lateinit var employeeDAO: EmployeeDAOI

    fun checkPassword(user: Employee, pass:String):Boolean {
        return BCrypt.checkpw(pass,user.password)
    }

    fun setPassword(user: Employee, pass:String):Boolean {
        user.password = BCrypt.hashpw(pass, BCrypt.gensalt())
        return BCrypt.checkpw(pass,user.password)
    }

    fun updateUser(user: Employee): Employee? {
        val validEmployee = employeeDAO.findByUsername(user.username)
        if (validEmployee.isPresent) {
            return employeeDAO.save(user)
        }
        return null
    }

    open fun isValidEmployee(user: Employee): Boolean {
        if (user.username.length < 1) {
            return false
        } else if (user.password.length < 1) {
            return false
        }
        return true
    }
}