package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("Effective Java");
        sampleBook.setAuthor("Joshua Bloch");
        sampleBook.setIsbn("9780134685991");
        sampleBook.setPublishedDate(LocalDate.of(2018, 1, 6));
    }

    @Test
    @DisplayName("getAllBooks - Positive: Returns all books")
    void testGetAllBooks_Positive() {
        List<Book> books = Arrays.asList(sampleBook);
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        List<Book> result = bookService.getAllBooks();
        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
    }

    @Test
    @DisplayName("getAllBooks - Edge: Empty List")
    void testGetAllBooks_Empty() {
        Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList());
        List<Book> result = bookService.getAllBooks();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getBookById - Positive: Found")
    void testGetBookById_Positive() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        Book result = bookService.getBookById(1L);
        assertEquals("Effective Java", result.getTitle());
    }

    @Test
    @DisplayName("getBookById - Negative: Not Found")
    void testGetBookById_NotFound() {
        Mockito.when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(999L));
        assertEquals("Book not found with id: 999", ex.getMessage());
    }

    @Test
    @DisplayName("createBook - Positive: Create new book")
    void testCreateBook_Positive() {
        Book toCreate = new Book();
        toCreate.setTitle("Clean Code");
        toCreate.setAuthor("Robert C. Martin");
        toCreate.setIsbn("9780132350884");
        toCreate.setPublishedDate(LocalDate.of(2008, 8, 1));
        Book created = new Book();
        created.setId(2L);
        created.setTitle("Clean Code");
        created.setAuthor("Robert C. Martin");
        created.setIsbn("9780132350884");
        created.setPublishedDate(LocalDate.of(2008, 8, 1));
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(created);
        Book result = bookService.createBook(toCreate);
        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    @DisplayName("updateBook - Positive: Update book details")
    void testUpdateBook_Positive() {
        Book updatedDetails = new Book();
        updatedDetails.setTitle("Effective Java 3rd Edition");
        updatedDetails.setAuthor("Joshua Bloch");
        updatedDetails.setIsbn("9780134685991");
        updatedDetails.setPublishedDate(LocalDate.of(2018, 1, 6));
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Book result = bookService.updateBook(1L, updatedDetails);
        assertEquals("Effective Java 3rd Edition", result.getTitle());
    }

    @Test
    @DisplayName("updateBook - Negative: Not Found")
    void testUpdateBook_NotFound() {
        Book updatedDetails = new Book();
        updatedDetails.setTitle("New Title");
        Mockito.when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(999L, updatedDetails));
        assertEquals("Book not found with id: 999", ex.getMessage());
    }

    @Test
    @DisplayName("deleteBook - Positive: Delete book")
    void testDeleteBook_Positive() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        Mockito.doNothing().when(bookRepository).delete(sampleBook);
        assertDoesNotThrow(() -> bookService.deleteBook(1L));
    }

    @Test
    @DisplayName("deleteBook - Negative: Not Found")
    void testDeleteBook_NotFound() {
        Mockito.when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(999L));
        assertEquals("Book not found with id: 999", ex.getMessage());
    }
}
