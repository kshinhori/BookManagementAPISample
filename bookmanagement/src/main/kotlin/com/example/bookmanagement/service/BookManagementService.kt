package com.example.bookmanagement.service

import com.example.bookmanagement.dto.BookAuthorDto
import com.example.bookmanagement.model.Book
import org.springframework.stereotype.Service
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field

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

}

