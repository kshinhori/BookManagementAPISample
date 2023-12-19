package com.example.bookmanagement

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class BookmanagementApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	fun contextLoads() {
	}

	@Test
	fun testSearchBookByTitle_Success() {
		// タイトルによる書籍の検索が成功するケース
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmanagement/search/books?title=The Tale of Peter Rabbit"))
				.andExpect(MockMvcResultMatchers.status().isOk)
	}

	@Test
	fun testSearchBookByAuthorName_Success() {
		// 著者名による書籍の検索が成功するケース
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmanagement/search/books?authorName=Beatrix Potter"))
				.andExpect(MockMvcResultMatchers.status().isOk)
	}

	@Test
	fun testSearchBookByTitleAndAuthorName_Success() {
		// タイトル・著者名による書籍の検索が成功するケース
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmanagement/search/books?title=The Tale of Peter Rabbit&authorName=Beatrix Potter"))
				.andExpect(MockMvcResultMatchers.status().isOk)
	}

	@Test
	fun testSearchBookWithoutTitleAndAuthorName_Success() {
		// タイトル・著者名なしで書籍の検索が成功するケース
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmanagement/search/books"))
				.andExpect(MockMvcResultMatchers.status().isOk)
	}

	@Test
	fun testSearchBookByTitle_NotFound() {
		// タイトルによる書籍の検索が失敗するケース
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmanagement/search/books?title=Not Found Test"))
				.andExpect(MockMvcResultMatchers.status().isNotFound)
	}

	@Test
	fun testSearchBookByAuthorName_NotFound() {
		// タイトルによる書籍の検索が失敗するケース
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmanagement/search/books?authorName=Not Found Test"))
				.andExpect(MockMvcResultMatchers.status().isNotFound)
	}

	@Test
	fun testCreateBook_Success() {
		// 書籍情報登録が成功するケース
		val newBookJson = """
            {
                "title": "New Book Title",
                "authorId": 1
            }
        """.trimIndent()

		mockMvc.perform(MockMvcRequestBuilders.post("/bookmanagement/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBookJson))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Book Title"))
	}

	@Test
	fun testCreateBook_BadRequest() {
		// 書籍情報登録が失敗するケース(タイトルが空)
		val newBookJson = """
            {
                "title": "",
                "authorId": 1
            }
        """.trimIndent()

		mockMvc.perform(MockMvcRequestBuilders.post("/bookmanagement/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBookJson))
				.andExpect(MockMvcResultMatchers.status().isBadRequest)
	}

	@Test
	fun testCreateBook_NotFound() {
		// 書籍情報登録が失敗するケース(著者情報が見つからない)
		val newBookJson = """
            {
                "title": "New Book Title",
                "authorId": 0
            }
        """.trimIndent()

		mockMvc.perform(MockMvcRequestBuilders.post("/bookmanagement/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newBookJson))
				.andExpect(MockMvcResultMatchers.status().isNotFound)
	}
}
