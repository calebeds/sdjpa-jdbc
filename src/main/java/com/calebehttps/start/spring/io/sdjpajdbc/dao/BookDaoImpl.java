package com.calebehttps.start.spring.io.sdjpajdbc.dao;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class BookDaoImpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book findBookById(Long id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?", this.getRowMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return this.jdbcTemplate.queryForObject( "SELECT * FROM book WHERE title = ?", this.getRowMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (title, isbn, publisher, author_id) VALUES (?, ?, ?, ?)", book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.findBookById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book SET title = ?, isbn = ?, publisher = ?, author_id = ?", book.getTitle(), book.getIsbn(), book.getPublisher(), book.getAuthorId());

        return this.findBookById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    private RowMapper<Book> getRowMapper() {
        return new BookMapper();
    }
}
