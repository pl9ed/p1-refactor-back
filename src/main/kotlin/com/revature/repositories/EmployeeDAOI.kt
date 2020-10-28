package com.revature.repositories

import com.revature.models.Employee
import com.revature.models.enums.EmployeeType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDAOI: CrudRepository<Employee, Int> {
    fun findByUsername(username: String): Employee?
    fun save(user: Employee): Employee
    fun existsByUsername(username:String): Boolean
}