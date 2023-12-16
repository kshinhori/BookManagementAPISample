package com.example.bookmanagement.controller

import com.example.bookmanagement.dto.BookAuthorDto
import com.example.bookmanagement.model.Book
import com.example.bookmanagement.service.BookManagementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bookmanagement")
class BookManagementController(private val bookManagementService: BookManagementService) {

    // タイトル・著者名に基づく書籍情報検索(タイトル・著者名未指定の場合は全件取得)
    @GetMapping("/search/books")
    fun searchBooks(
            @RequestParam(required = false) title: String?,
            @RequestParam(required = false) authorName: String?
    ): ResponseEntity<Any> {
        val books = bookManagementService.searchBooks(title, authorName)
        return if (books.isNotEmpty()) {
            ResponseEntity.ok(books)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("errorCode" to "notfound"))
        }
    }

}
