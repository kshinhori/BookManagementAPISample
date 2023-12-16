package com.example.bookmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan("com.example.bookmanagement.model")
class BookmanagementApplication

fun main(args: Array<String>) {
	runApplication<BookmanagementApplication>(*args)
}
