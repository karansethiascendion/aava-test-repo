package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book getSampleBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("9780134685991");
        book.setPublishedDate(LocalDate.of(2018, 1, 6));
        return book;
    }

    @Test
    @DisplayName("GET /api/books - Positive: Returns list of books")
    void testGetAllBooks_Positive() throws Exception {
        List<Book> books = Arrays.asList(getSampleBook());
        Mockito.when(bookService.getAllBooks()).thenReturn(books);
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    @DisplayName("GET /api/books - Edge: Empty list")
    void testGetAllBooks_Empty() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/books/{id} - Positive: Found")
    void testGetBookById_Positive() throws Exception {
        Mockito.when(bookService.getBookById(1L)).thenReturn(getSampleBook());
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    @DisplayName("GET /api/books/{id} - Negative: Not Found")
    void testGetBookById_NotFound() throws Exception {
        Mockito.when(bookService.getBookById(999L)).thenThrow(new ResourceNotFoundException("Book not found with id: 999"));
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book not found with id: 999"));
    }

    @Test
    @DisplayName("POST /api/books - Positive: Create Book")
    void testCreateBook_Positive() throws Exception {
        Book input = getSampleBook();
        input.setId(null);
        Book created = getSampleBook();
        Mockito.when(bookService.createBook(any(Book.class))).thenReturn(created);
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/books - Negative: Invalid ISBN")
    void testCreateBook_InvalidISBN() throws Exception {
        Book input = getSampleBook();
        input.setIsbn("INVALID_ISBN");
        input.setId(null);
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isbn").exists());
    }

    @Test
    @DisplayName("POST /api/books - Negative: Missing Title")
    void testCreateBook_MissingTitle() throws Exception {
        Book input = getSampleBook();
        input.setTitle("");
        input.setId(null);
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is mandatory"));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Positive: Update Book")
    void testUpdateBook_Positive() throws Exception {
        Book updated = getSampleBook();
        updated.setTitle("Effective Java 3rd Edition");
        Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updated);
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java 3rd Edition"));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Negative: Not Found")
    void testUpdateBook_NotFound() throws Exception {
        Book input = getSampleBook();
        Mockito.when(bookService.updateBook(eq(999L), any(Book.class))).thenThrow(new ResourceNotFoundException("Book not found with id: 999"));
        mockMvc.perform(put("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book not found with id: 999"));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Negative: Invalid Author")
    void testUpdateBook_InvalidAuthor() throws Exception {
        Book input = getSampleBook();
        input.setAuthor("");
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.author").value("Author is mandatory"));
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Positive: Delete Book")
    void testDeleteBook_Positive() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Negative: Not Found")
    void testDeleteBook_NotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Book not found with id: 999")).when(bookService).deleteBook(999L);
        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book not found with id: 999"));
    }
}
