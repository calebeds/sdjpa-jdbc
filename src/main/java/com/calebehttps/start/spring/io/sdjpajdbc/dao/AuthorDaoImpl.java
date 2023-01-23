package com.calebehttps.start.spring.io.sdjpajdbc.dao;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        String sql = "SELECT author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author left outer join book on author.id = book.author_id where author.id = ?";
//        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE id = ?", getRowMapper(), id);
        return jdbcTemplate.query(sql, new AuthorExtractor(), id);
    }

    @Override
    public Author getAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ? and last_name = ?",
                getRowMapper(), firstName, lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
                author.getFirstName(), author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(), author.getLastName(), author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }
}
