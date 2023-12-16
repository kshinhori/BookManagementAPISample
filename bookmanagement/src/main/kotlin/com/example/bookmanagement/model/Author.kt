package com.example.bookmanagement.model

import javax.persistence.*

@Entity
class Author(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var authorId: Long,

        @Column(nullable = false)
        var name: String,

        @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
        var books: MutableList<Book> = mutableListOf()
)
