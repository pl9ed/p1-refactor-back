package com.revature.controllers

import com.revature.models.Employee
import com.revature.models.EmployeeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.revature.repositories.EmployeeDAOI

@RestController
class EmployeeController {

    @Autowired
    private lateinit var employeeDAO: EmployeeDAOI

    @GetMapping("/employee/{username}")
    fun getEmployee(@PathVariable username: String): ResponseEntity<Employee> {
        val employee: Employee? = employeeDAO.findByUsername(username)
        return if (employee != null) {
            ResponseEntity.ok(employee)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/employee")
    fun postEmployee(@RequestBody employeeDTO: EmployeeDTO): ResponseEntity<Employee> {
        val employee = employeeDTO.toEmployee()
        val response = employeeDAO.save(employee)

        return ResponseEntity.status(201).body(response)
    }

}