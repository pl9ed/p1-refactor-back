package com.revature.p1refactorback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@ComponentScan("com.revature")
@EnableJpaRepositories("com.revature.repositories")
@EntityScan("com.revature.models")
@SpringBootApplication
class P1RefactorBackApplication

fun main(args: Array<String>) {
	runApplication<P1RefactorBackApplication>(*args)
}
