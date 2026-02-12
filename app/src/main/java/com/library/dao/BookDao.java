package com.library.dao;

import com.library.model.Book;
import java.util.List;

public interface BookDao {
    List<Book> findAll();
    Book findById(Long id);
    Book save(Book book);
    Book update(Book book);
    void delete(Long id);
}
