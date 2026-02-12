package com.library.dao.impl;

import com.library.dao.BookDao;
import com.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> rowMapper = new RowMapper<Book>() {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setIsbn(rs.getString("isbn"));
            book.setPublishedDate(rs.getDate("published_date").toLocalDate());
            return book;
        }
    };

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM books", rowMapper);
    }

    @Override
    public Book findById(Long id) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE id = ?", rowMapper, id);
        return books.isEmpty() ? null : books.get(0);
    }

    @Override
    public Book save(Book book) {
        jdbcTemplate.update(
            "INSERT INTO books (title, author, isbn, published_date) VALUES (?, ?, ?, ?)",
            book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate()
        );
        // Retrieve the last inserted id (H2/Postgres/MySQL compatible)
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return findById(id);
    }

    @Override
    public Book update(Book book) {
        jdbcTemplate.update(
            "UPDATE books SET title = ?, author = ?, isbn = ?, published_date = ? WHERE id = ?",
            book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate(), book.getId()
        );
        return findById(book.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }
}
