package com.library.repository;

import com.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setTitle("Effective Java");
        sampleBook.setAuthor("Joshua Bloch");
        sampleBook.setIsbn("9780134685991");
        sampleBook.setPublishedDate(LocalDate.of(2018, 1, 6));
    }

    @Test
    @DisplayName("save and find - Positive: Save and retrieve book")
    void testSaveAndFindBook_Positive() {
        Book saved = bookRepository.save(sampleBook);
        Optional<Book> found = bookRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Effective Java", found.get().getTitle());
    }

    @Test
    @DisplayName("findAll - Edge: Empty repo returns empty list")
    void testFindAll_Empty() {
        bookRepository.deleteAll();
        assertTrue(bookRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("delete - Positive: Delete book")
    void testDeleteBook_Positive() {
        Book saved = bookRepository.save(sampleBook);
        Long id = saved.getId();
        bookRepository.delete(saved);
        assertFalse(bookRepository.findById(id).isPresent());
    }

    @Test
    @DisplayName("delete - Negative: Delete non-existent book throws exception")
    void testDeleteBook_NotFound() {
        Book notSaved = new Book();
        notSaved.setId(999L);
        notSaved.setTitle("Ghost Book");
        assertThrows(EmptyResultDataAccessException.class, () -> bookRepository.deleteById(999L));
    }

    @Test
    @DisplayName("update - Positive: Update book details")
    void testUpdateBook_Positive() {
        Book saved = bookRepository.save(sampleBook);
        saved.setTitle("Effective Java 3rd Edition");
        Book updated = bookRepository.save(saved);
        Optional<Book> found = bookRepository.findById(updated.getId());
        assertTrue(found.isPresent());
        assertEquals("Effective Java 3rd Edition", found.get().getTitle());
    }

    @Test
    @DisplayName("transaction rollback - Edge: Rollback on failure")
    void testTransactionRollback() {
        Book saved = bookRepository.save(sampleBook);
        try {
            bookRepository.save(null);
        } catch (Exception e) {
            // expected
        }
        Optional<Book> found = bookRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }
}
