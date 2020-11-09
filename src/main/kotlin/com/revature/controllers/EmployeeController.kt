package com.revature.controllers

import com.fasterxml.jackson.core.JsonParseException
import com.revature.models.Employee
import com.revature.models.EmployeeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.revature.repositories.EmployeeDAOI
import com.revature.services.EmployeeService

@RestController
class EmployeeController {

    @Autowired
    private lateinit var employeeDAO: EmployeeDAOI

    @Autowired
    private lateinit var employeeService: EmployeeService

    @GetMapping("/employee/{username}")
    fun getEmployee(@PathVariable username: String): ResponseEntity<Employee> {
        val employee = employeeDAO.findByUsername(username)
        return if (employee.isPresent) {
            ResponseEntity.ok(employee.get())
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @GetMapping("/employee")
    fun getAllEmployees(): ResponseEntity<Iterable<Employee>> =
            ResponseEntity.status(200).body(employeeDAO.findAll())

    @PostMapping("/employee")
    fun postEmployee(@RequestBody employeeDTO: EmployeeDTO): ResponseEntity<Employee> {
        try {
            val employee = employeeDTO.toEmployee()

            if (employeeService.isValidEmployee(employee)) {
                val response = employeeDAO.save(employee)
                return ResponseEntity.status(201).body(response)
            }
            return ResponseEntity.badRequest().build()
        } catch (e: JsonParseException) {
            // TODO log
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/login", consumes=["multipart/form-data"])
    fun login(@RequestParam("username") username:String,
              @RequestParam("password") password:String): ResponseEntity<Employee> {
        val user = employeeService.login(username, password) ?: return ResponseEntity.status(401).build()
        return ResponseEntity.ok(user)
    }

}