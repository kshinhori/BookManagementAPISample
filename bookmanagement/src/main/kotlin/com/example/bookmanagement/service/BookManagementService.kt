package com.example.bookmanagement.service

import com.example.bookmanagement.dto.BookAuthorDto
import com.example.bookmanagement.model.Author
import com.example.bookmanagement.model.Book
import org.springframework.stereotype.Service
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Service
class BookManagementService(private val dsl: DSLContext) {

    fun searchBooks(title: String?, authorName: String?): List<BookAuthorDto> {
        var condition = DSL.noCondition()
        if (!title.isNullOrEmpty()) {
            condition = condition.and(field("Books.title", String::class.java).eq(title))
        }
        if (!authorName.isNullOrEmpty()) {
            condition = condition.and(field("Authors.name", String::class.java).eq(authorName))
        }

        val records = dsl.select()
                .from("Books")
                .join("Authors").on("Books.author_id = Authors.author_id")
                .where(condition)
                .fetch()

        return records.map {
            BookAuthorDto(
                    bookId = it.get(field("BOOK_ID", Long::class.java)),
                    title = it.get(field("TITLE", String::class.java)),
                    authorName = it.get(field("NAME", String::class.java))
            )
        }
    }

    fun createBook(book: Book): ResponseEntity<Any> {
        // タイトルと著者IDのバリデーション
        if (book.title.isBlank()) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Invalid book data"))
        }

        // 著者IDがAuthorsテーブルに存在するかチェック
        val authorExists = (dsl.selectCount()
                .from("Authors")
                .where(field("AUTHOR_ID", Long::class.java).eq(book.authorId))
                .fetchOne()?.value1() ?: 0) > 0

        if (!authorExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Author not found"))
        }

        // 書籍の登録処理
        val newBookId = dsl.insertInto(table("Books"))
                .set(field("TITLE", String::class.java), book.title)
                .set(field("AUTHOR_ID", Long::class.java), book.authorId)
                .returning(field("BOOK_ID", Long::class.java))
                .fetchOne()
                ?.getValue(field("BOOK_ID", Long::class.java))

        book.bookId = newBookId ?: return ResponseEntity.internalServerError().body(mapOf("error" to "Failed to create book"))
        return ResponseEntity.ok(book)
    }

    fun updateBook(bookId: Long, book: Book): ResponseEntity<Any> {
        // 書籍IDの存在チェック
        val bookExists = (dsl.selectCount()
                .from("Books")
                .where(field("book_id", Long::class.java).eq(bookId))
                .fetchOne(0, Int::class.java) ?: 0) > 0

        if (!bookExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("errorCode" to "notfound"))
        }

        // タイトルが空でないかチェック
        if (book.title.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(mapOf("errorCode" to "notaddeptable", "message" to "Title is required"))
        }
        // 著者IDがAuthorsテーブルに存在するかチェック
        val authorExists = (dsl.selectCount()
                .from("Authors")
                .where(field("AUTHOR_ID", Long::class.java).eq(book.authorId))
                .fetchOne()?.value1() ?: 0) > 0

        if (!authorExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Author not found"))
        }

        val updatedRows = dsl.update(table("Books"))
                .set(field("TITLE", String::class.java), book.title)
                .set(field("AUTHOR_ID", Long::class.java), book.authorId)
                .where(field("BOOK_ID", Long::class.java).eq(bookId))
                .execute()

        return if (updatedRows > 0) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.internalServerError().body(mapOf("error" to "Failed to update book"))
        }
    }

    fun createAuthor(author: Author): ResponseEntity<Any> {
        // 名前が空でないかチェック
        if (author.name.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(mapOf("errorCode" to "notacceptable", "message" to "Name is required"))
        }

        val authorId = dsl.insertInto(table("Authors"))
                .set(field("NAME", String::class.java), author.name)
                .returning(field("AUTHOR_ID"))
                .fetchOne()
                ?.getValue(field("AUTHOR_ID", Long::class.java))

        return if (authorId != null) {
            ResponseEntity.ok().body(mapOf("authorId" to authorId))
        } else {
            ResponseEntity.internalServerError().body(mapOf("error" to "Failed to create author"))
        }
    }

}

