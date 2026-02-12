package com.library.service.impl;

import com.library.dao.BookDao;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;

    @Override
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        Book book = bookDao.findById(id);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        return book;
    }

    @Override
    @Transactional
    public Book createBook(Book book) {
        return bookDao.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Long id, Book book) {
        Book existing = bookDao.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        book.setId(id);
        return bookDao.update(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book existing = bookDao.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookDao.delete(id);
    }
}
