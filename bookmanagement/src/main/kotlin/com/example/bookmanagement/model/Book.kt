package com.example.bookmanagement.model

import javax.persistence.*

@Entity
class Book(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var bookId: Long,

        @Column(nullable = false)
        var title: String,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "author_id")
        var author: Long

)
