package com.example.bookmanagement.controller

import com.example.bookmanagement.model.Author
import com.example.bookmanagement.model.Book
import com.example.bookmanagement.service.BookManagementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bookmanagement")
class BookManagementController(private val bookManagementService: BookManagementService) {

    // タイトル・著者名に基づく書籍情報検索(タイトル・著者名未指定の場合は全件取得)
    @GetMapping("/books")
    fun searchBooks(
            @RequestParam(required = false) title: String?,
            @RequestParam(required = false) authorName: String?
    ): ResponseEntity<Any> {
        return bookManagementService.searchBooks(title, authorName)
    }

    // 書籍情報登録API
    @PostMapping("/books")
    fun createBooks(@RequestBody book: Book): ResponseEntity<Any> {
        return bookManagementService.createBook(book)
    }

    // 書籍情報更新API
    @PutMapping("/books/{bookId}")
    fun updateBook(@PathVariable bookId: Long, @RequestBody book: Book): ResponseEntity<Any> {
        return bookManagementService.updateBook(bookId, book)
    }

    // 著者情報登録API
    @PostMapping("/authors")
    fun createAuthor(@RequestBody author: Author): ResponseEntity<Any> {
        return bookManagementService.createAuthor(author)
    }

    // 著者情報更新API
    @PutMapping("/authors/{authorId}")
    fun updateAuthor(@PathVariable authorId: Long, @RequestBody author: Author): ResponseEntity<Any> {
        return bookManagementService.updateAuthor(authorId, author)
    }
}
